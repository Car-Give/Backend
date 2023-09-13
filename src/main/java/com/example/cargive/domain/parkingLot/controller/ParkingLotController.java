package com.example.cargive.domain.parkingLot.controller;

import com.example.cargive.domain.parkingLot.dto.request.ParkingLotCreateRequest;
import com.example.cargive.domain.parkingLot.dto.response.ParkingLotInfo;
import com.example.cargive.domain.parkingLot.dto.response.ParkingLotSliceInfo;
import com.example.cargive.domain.parkingLot.service.ParkingLotService;
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
         ParkingLotInfo parkingLotInfo = parkingLotService.createParkingLot(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ResultResponse.of(ResultCode.PARKING_LOT_CREATE_SUCCESS, parkingLotInfo));
    }

    @DeleteMapping("/{parkingLotId}")
    public ResponseEntity<ResultResponse> deleteParkingLot(@PathVariable Long parkingLotId) {
        parkingLotService.deleteParkingLot(parkingLotId);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.PARKING_LOT_DELETE_SUCCESS));
    }

    @GetMapping
    public ResponseEntity<ResultResponse> findParkingLots(
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "100") int size,
            @RequestParam Long favoriteId,
            @RequestParam Long parkingLotId){

        ParkingLotSliceInfo parkingLots = parkingLotService.findParkingLots(offset, size, favoriteId, parkingLotId);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.PARKING_LOT_READ_SUCCESS, parkingLots));

    }
}
