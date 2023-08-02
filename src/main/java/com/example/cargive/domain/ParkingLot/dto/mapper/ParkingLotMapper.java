package com.example.cargive.domain.ParkingLot.dto.mapper;

import com.example.cargive.domain.Favorites.entity.Favorite;
import com.example.cargive.domain.ParkingLot.dto.request.ParkingLotCreateRequest;
import com.example.cargive.domain.ParkingLot.entity.ParkingLot;
import org.springframework.stereotype.Component;

@Component
public class ParkingLotMapper {

    public ParkingLot toCreateRequest(ParkingLotCreateRequest request, Favorite favorite){
        return ParkingLot.builder()
                .name(request.getParkingLotName())
                .phoneNumber(request.getPhoneNumber())
                .address(request.getAddress())
                .fee(request.getFee())
                .favorite(favorite)
                .build();
    }

}
