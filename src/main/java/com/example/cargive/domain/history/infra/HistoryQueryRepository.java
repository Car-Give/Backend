package com.example.cargive.domain.history.infra;

import com.example.cargive.domain.history.controller.dto.HistoryResponse;

import java.util.List;

public interface HistoryQueryRepository {
    // 특정 차량의 이용 기록을 조회하는 메서드, 종료된 이용기록이 먼저 조회되도록 정렬 기준 설정
    List<HistoryResponse> findHistoryListByCarId(Long carId, Long memberId);
}
