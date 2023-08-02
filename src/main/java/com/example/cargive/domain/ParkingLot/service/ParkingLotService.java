package com.example.cargive.domain.ParkingLot.service;

import com.example.cargive.domain.Favorites.entity.Favorite;
import com.example.cargive.domain.Favorites.service.FavoriteService;
import com.example.cargive.domain.ParkingLot.dto.mapper.ParkingLotMapper;
import com.example.cargive.domain.ParkingLot.dto.request.ParkingLotCreateRequest;
import com.example.cargive.domain.ParkingLot.repository.ParkingLotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
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

}
