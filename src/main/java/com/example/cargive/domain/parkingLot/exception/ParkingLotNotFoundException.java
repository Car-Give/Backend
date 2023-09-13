package com.example.cargive.domain.parkingLot.exception;

import com.example.cargive.global.base.BaseResponseStatus;
import com.example.cargive.global.exception.BusinessException;

public class ParkingLotNotFoundException extends BusinessException {
    public ParkingLotNotFoundException() {
        super(BaseResponseStatus.PARKING_LOT_NOT_FOUND_ERROR);
    }
}
