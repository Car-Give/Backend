package com.example.cargive.domain.car.infra;

import com.example.cargive.domain.car.controller.dto.response.CarResponse;

import java.util.List;

public interface CarQueryRepository {
    // 사용자의 PK값을 통하여 사용자가 등록한 차량의 정보를 조회하는 메서드
    List<CarResponse> findCarByMemberId(Long memberId);
}