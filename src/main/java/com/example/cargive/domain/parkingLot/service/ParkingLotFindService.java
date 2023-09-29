package com.example.cargive.domain.parkingLot.service;

import com.example.cargive.domain.parkingLot.entity.ParkingLot;
import com.example.cargive.domain.parkingLot.entity.ParkingLotRepository;
import com.example.cargive.global.base.BaseException;
import com.example.cargive.global.base.BaseResponseStatus;
import com.example.cargive.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// Repository를 외부에 직접적으로 노출하는 것을 피하기 위하여 별도의 Entity 조회 서비스 생성
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ParkingLotFindService {
    private final ParkingLotRepository parkingLotRepository;

    // PK값을 통하여 Entity를 조회, 존재하지 않을 경우 오류를 반환
    public ParkingLot findParkingLotById(Long parkingLotId) {
        return parkingLotRepository.findById(parkingLotId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.PARKING_LOT_NOT_FOUND_ERROR));
    }
}