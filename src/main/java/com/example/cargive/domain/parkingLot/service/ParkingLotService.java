package com.example.cargive.domain.parkingLot.service;

import com.example.cargive.domain.parkingLot.controller.dto.request.ParkingLotRequest;
import com.example.cargive.domain.parkingLot.controller.dto.response.ParkingLotResponse;
import com.example.cargive.domain.parkingLot.entity.ParkingLot;
import com.example.cargive.domain.parkingLot.entity.ParkingLotRepository;
import com.example.cargive.global.base.BaseException;
import com.example.cargive.global.base.BaseResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ParkingLotService {
    private final ParkingLotRepository parkingLotRepository;

    // Entity의 PK값을 통하여 단일 조회하는 기능, DTO 파일로 데이터 반환
    @Transactional(readOnly = true)
    public ParkingLotResponse findParkingLot(Long parkingLotId) {
        ParkingLot findValue = getParkingLotById(parkingLotId);
        return ParkingLotResponse.toDto(findValue);
    }

    // 사용자로부터 위도와 경도를 입력받아 별도의 주차장 Entity를 생성하는 기능
    @Transactional
    public void createParkingLot(ParkingLotRequest request) {
        ParkingLot parkingLot = request.toParkingLot();
        parkingLotRepository.save(parkingLot);
    }

    // Entity의 PK값을 통하여 삭제하는 기능
    @Transactional
    public void deleteParkingLot(Long parkingLotId) {
        ParkingLot findValue = getParkingLotById(parkingLotId);
        findValue.deleteEntity();
    }

    // Entity의 PK값을 통해 데이터를 조회하는 메서드, 일치하는 데이터가 존재하지 않을 경우 오류 반환
    private ParkingLot getParkingLotById(Long parkingLotId) {
        return parkingLotRepository.findById(parkingLotId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.PARKING_LOT_NOT_FOUND_ERROR));
    }
}