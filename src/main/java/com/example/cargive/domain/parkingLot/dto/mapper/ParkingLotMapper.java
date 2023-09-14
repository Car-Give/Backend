package com.example.cargive.domain.parkingLot.dto.mapper;

import com.example.cargive.domain.favorite.entity.Favorite;
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
        return null;
    }

    public ParkingLotInfo toEntity(ParkingLot parkingLot){
        return null;
    }

    public ParkingLotSliceInfo toParkingLotInfoList(Slice<ParkingLot> parkingLots){
        List<ParkingLotInfo> parkingLotInfos = parkingLots.stream().map(this::toEntity).toList();
        return ParkingLotSliceInfo.builder().parkingLotInfoList(parkingLotInfos).build();
    }

}
