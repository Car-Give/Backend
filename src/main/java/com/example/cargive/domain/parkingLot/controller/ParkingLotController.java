package com.example.cargive.domain.parkingLot.controller;

import com.example.cargive.domain.parkingLot.controller.dto.request.ParkingLotRequest;
import com.example.cargive.domain.parkingLot.controller.dto.response.ParkingLotResponse;
import com.example.cargive.domain.parkingLot.service.ParkingLotService;
import com.example.cargive.global.base.BaseResponse;
import com.example.cargive.global.base.BaseResponseStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/parkinglot")
public class ParkingLotController {
    private final ParkingLotService parkingLotService;

    @GetMapping("/{parkingLotId}") // 주차장 단일 조회
    public ResponseEntity<BaseResponse> findParkingLot(@PathVariable Long parkingLotId) {
        ParkingLotResponse findPkLot = parkingLotService.findParkingLot(parkingLotId);
        return BaseResponse.toResponseEntityContainsResult(findPkLot);
    }

    @PostMapping // 주차장 추가
    public ResponseEntity<BaseResponse> createParkingLot(@RequestBody @Valid ParkingLotRequest request) {
        parkingLotService.createParkingLot(request);
        return BaseResponse.toResponseEntityContainsStatus(BaseResponseStatus.CREATED);
    }

    @DeleteMapping("/{parkingLotId}") // 주차장 삭제
    public ResponseEntity<BaseResponse> deleteParkingLot(@PathVariable Long parkingLotId) {
        parkingLotService.deleteParkingLot(parkingLotId);
        return BaseResponse.toResponseEntityContainsStatus(BaseResponseStatus.SUCCESS);
    }
}