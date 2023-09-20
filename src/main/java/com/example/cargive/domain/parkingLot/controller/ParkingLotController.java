package com.example.cargive.domain.parkingLot.controller;

import com.example.cargive.domain.parkingLot.controller.dto.request.ParkingLotRequest;
import com.example.cargive.domain.parkingLot.controller.dto.response.ParkingLotResponse;
import com.example.cargive.domain.parkingLot.service.ParkingLotService;
import com.example.cargive.global.base.BaseResponse;
import com.example.cargive.global.base.BaseResponseStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/parkinglot")
public class ParkingLotController {
    private final ParkingLotService parkingLotService;

    @GetMapping("/{parkingLotId}") // 주차장 단일 조회
    public BaseResponse<ParkingLotResponse> findParkingLot(@PathVariable Long parkingLotId) {
        ParkingLotResponse findPkLot = parkingLotService.findParkingLot(parkingLotId);
        return new BaseResponse<>(BaseResponseStatus.SUCCESS, findPkLot);
    }

    @PostMapping // 주차장 추가
    public BaseResponse<BaseResponseStatus> createParkingLot(@RequestBody @Valid ParkingLotRequest request) {
        parkingLotService.createParkingLot(request);
        return new BaseResponse<>(BaseResponseStatus.CREATED);
    }

    @DeleteMapping("/{parkingLotId}") // 주차장 삭제
    public BaseResponse<BaseResponseStatus> deleteParkingLot(@PathVariable Long parkingLotId) {
        parkingLotService.deleteParkingLot(parkingLotId);
        return new BaseResponse<>(BaseResponseStatus.SUCCESS);
    }
}