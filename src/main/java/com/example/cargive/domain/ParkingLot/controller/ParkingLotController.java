package com.example.cargive.domain.ParkingLot.controller;

import com.example.cargive.domain.ParkingLot.dto.request.ParkingLotCreateRequest;
import com.example.cargive.domain.ParkingLot.service.ParkingLotService;
import com.example.cargive.global.result.ResultCode;
import com.example.cargive.global.result.ResultResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/favorites/parking_lot")
public class ParkingLotController {

    private final ParkingLotService parkingLotService;

    @PostMapping
    public ResponseEntity<ResultResponse> createParkingLot(@Valid @RequestBody ParkingLotCreateRequest request){
        parkingLotService.createParkingLot(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ResultResponse.of(ResultCode.PARKING_LOT_CREATE_SUCCESS));
    }

    @DeleteMapping("/{parkingLotId}")
    public ResponseEntity<ResultResponse> deleteParkingLot(@PathVariable Long parkingLotId) {
        parkingLotService.deleteParkingLot(parkingLotId);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.PARKING_LOT_DELETE_SUCCESS));
    }
}
