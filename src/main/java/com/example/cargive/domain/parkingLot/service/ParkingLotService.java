package com.example.cargive.domain.parkingLot.service;

import com.example.cargive.domain.favorite.entity.Favorite;
import com.example.cargive.domain.favorite.service.FavoriteService;
import com.example.cargive.domain.parkingLot.dto.mapper.ParkingLotMapper;
import com.example.cargive.domain.parkingLot.dto.request.ParkingLotCreateRequest;
import com.example.cargive.domain.parkingLot.dto.response.ParkingLotInfo;
import com.example.cargive.domain.parkingLot.dto.response.ParkingLotSliceInfo;
import com.example.cargive.domain.parkingLot.entity.ParkingLot;
import com.example.cargive.domain.parkingLot.exception.ParkingLotNotFoundException;
import com.example.cargive.domain.parkingLot.repository.ParkingLotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ParkingLotService {

    private final ParkingLotRepository parkingLotRepository;
    private final ParkingLotMapper parkingLotMapper;
    private final FavoriteService favoriteService;


    public ParkingLotInfo createParkingLot(ParkingLotCreateRequest request){
        Favorite findFavorite = favoriteService.findFavoriteById(request.getFavoriteId());
//        findFavorite.upCount();
        ParkingLot parkingLot =  parkingLotRepository.save(parkingLotMapper.toCreateRequest(request, findFavorite));
        return parkingLotMapper.toEntity(parkingLot);
    }

    public void deleteParkingLot(Long parkingLotId){
        ParkingLot parkingLot = findParkingLotById(parkingLotId);
//        parkingLot.updateFavoriteCount();
        parkingLotRepository.delete(parkingLot);
    }

    public ParkingLotSliceInfo findParkingLots(int offset, int size, Long favoriteId, Long parkingLotId){
//        PageRequest pageRequest = PageRequest.of(offset, size);
//        Slice<ParkingLot> parkingLots = parkingLotRepository.findParkingLotsByFavoriteId(favoriteId, parkingLotId, pageRequest);
//        return parkingLotMapper.toParkingLotInfoList(parkingLots);
        return null;
    }

    @Transactional(readOnly = true)
    public ParkingLot findParkingLotById(Long parkingLotId){
        return parkingLotRepository.findById(parkingLotId).orElseThrow(ParkingLotNotFoundException::new);
    }

}
