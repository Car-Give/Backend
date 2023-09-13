package com.example.cargive.domain.parkingLot.dto.mapper;

import com.example.cargive.domain.favorites.entity.Favorite;
import com.example.cargive.domain.parkingLot.dto.request.ParkingLotCreateRequest;
import com.example.cargive.domain.parkingLot.dto.response.ParkingLotInfo;
import com.example.cargive.domain.parkingLot.dto.response.ParkingLotSliceInfo;
import com.example.cargive.domain.parkingLot.entity.ParkingLot;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import java.util.List;

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

    public ParkingLotInfo toEntity(ParkingLot parkingLot){
        return ParkingLotInfo.builder()
                .parkingLotId(parkingLot.getId())
                .name(parkingLot.getName())
                .phoneNumber(parkingLot.getPhoneNumber())
                .address(parkingLot.getAddress())
                .fee(parkingLot.getFee())
                .build();
    }

    public ParkingLotSliceInfo toParkingLotInfoList(Slice<ParkingLot> parkingLots){
        List<ParkingLotInfo> parkingLotInfos = parkingLots.stream().map(this::toEntity).toList();
        return ParkingLotSliceInfo.builder().parkingLotInfoList(parkingLotInfos).build();
    }

}
