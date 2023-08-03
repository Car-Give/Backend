package com.example.cargive.domain.ParkingLot.service;

import com.example.cargive.domain.Favorites.entity.Favorite;
import com.example.cargive.domain.Favorites.service.FavoriteService;
import com.example.cargive.domain.ParkingLot.dto.mapper.ParkingLotMapper;
import com.example.cargive.domain.ParkingLot.dto.request.ParkingLotCreateRequest;
import com.example.cargive.domain.ParkingLot.dto.response.ParkingLotSliceInfo;
import com.example.cargive.domain.ParkingLot.entity.ParkingLot;
import com.example.cargive.domain.ParkingLot.exception.ParkingLotNotFoundException;
import com.example.cargive.domain.ParkingLot.repository.ParkingLotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ParkingLotService {

    private final ParkingLotRepository parkingLotRepository;
    private final ParkingLotMapper parkingLotMapper;
    private final FavoriteService favoriteService;


    public void createParkingLot(ParkingLotCreateRequest request){
        Favorite findFavorite = favoriteService.findFavoriteById(request.getFavoriteId());
        findFavorite.upCount();
        parkingLotRepository.save(parkingLotMapper.toCreateRequest(request, findFavorite));
    }

    public void deleteParkingLot(Long parkingLotId){
        ParkingLot parkingLot = findParkingLotById(parkingLotId);
        parkingLot.updateFavoriteCount();
        parkingLotRepository.delete(parkingLot);
    }

    public ParkingLotSliceInfo findParkingLots(int offset, int size, Long favoriteId, Long parkingLotId){
        PageRequest pageRequest = PageRequest.of(offset, size);
        Slice<ParkingLot> parkingLots = parkingLotRepository.findParkingLotsByFavoriteId(favoriteId, parkingLotId, pageRequest);
        return parkingLotMapper.toParkingLotInfoList(parkingLots);
    }

    @Transactional(readOnly = true)
    public ParkingLot findParkingLotById(Long parkingLotId){
        return parkingLotRepository.findById(parkingLotId).orElseThrow(ParkingLotNotFoundException::new);
    }

}
