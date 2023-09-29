package com.example.cargive.domain.car.service;

import com.example.cargive.domain.car.entity.Car;
import com.example.cargive.domain.car.entity.CarRepository;
import com.example.cargive.global.base.BaseException;
import com.example.cargive.global.base.BaseResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CarFindService {
    private final CarRepository carRepository;

    // Car Entity의 PK값을 통하여 데이터를 조회하는 메서드
    public Car getCar(Long carId) {
        return carRepository.findById(carId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.CAR_NOT_FOUND_ERROR));
    }
}
