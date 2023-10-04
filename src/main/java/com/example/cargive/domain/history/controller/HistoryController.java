package com.example.cargive.domain.history.controller;

import com.example.cargive.domain.history.service.HistoryService;
import com.example.cargive.global.base.BaseResponse;
import com.example.cargive.global.base.BaseResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/history")
public class HistoryController {
    private final HistoryService historyService;

    @GetMapping("/{carId}") // 자신 차량의 이용기록 조회
    public ResponseEntity<BaseResponse> getHistoryList(@PathVariable Long carId,
                                                       @RequestParam Long memberId) {
        return BaseResponse.toResponseEntityContainsResult(historyService.getHistoryList(carId, memberId));
    }

    @PostMapping("/{carId}") // 이용 시작
    public ResponseEntity<BaseResponse> startUsing(@PathVariable Long carId,
                                                   @RequestParam Long parkingLotId,
                                                   @RequestParam Long memberId) {
        historyService.startUseHistory(carId, parkingLotId, memberId);
        return BaseResponse.toResponseEntityContainsStatus(BaseResponseStatus.CREATED);
    }

    @PutMapping("/{historyId}") // 이용 종료
    public ResponseEntity<BaseResponse> endUsing(@PathVariable Long historyId,
                                                 @RequestParam Long carId,
                                                 @RequestParam Long memberId) {
        historyService.endUseHistory(historyId, carId, memberId);
        return BaseResponse.toResponseEntityContainsStatus(BaseResponseStatus.SUCCESS);
    }
}