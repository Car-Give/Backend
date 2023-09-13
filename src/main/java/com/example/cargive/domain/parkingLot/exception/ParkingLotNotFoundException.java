package com.example.cargive.domain.ParkingLot.exception;

import com.example.cargive.global.error.ErrorCode;
import com.example.cargive.global.exception.BusinessException;

public class ParkingLotNotFoundException extends BusinessException {
    public ParkingLotNotFoundException() {
        super(ErrorCode.PARKING_LOT_NOT_FOUND_ERROR);
    }
}
