package com.example.cargive.domain.favorite.controller;

import com.example.cargive.domain.favorite.service.FavoriteCarService;
import com.example.cargive.global.base.BaseResponse;
import com.example.cargive.global.base.BaseResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/favorite/car")
public class FavoriteCarController {
    private final FavoriteCarService favoriteCarService;

    @PostMapping("/{carId}") // 자신의 차량을 즐겨찾기에 등록
    public ResponseEntity<BaseResponse> createFavoriteCar(@PathVariable Long carId,
                                                          @RequestParam Long memberId) {
        favoriteCarService.createFavoriteCar(memberId, carId);
        return BaseResponse.toResponseEntityContainsStatus(BaseResponseStatus.CREATED);
    }

    @DeleteMapping("/{favoriteCarId}") // 차량을 즐겨찾기에서 삭제
    public ResponseEntity<BaseResponse> deleteFavoriteCar(@PathVariable Long favoriteCarId,
                                                          @RequestParam Long memberId) {
        favoriteCarService.deleteFavoriteCar(memberId, favoriteCarId);
        return BaseResponse.toResponseEntityContainsStatus(BaseResponseStatus.DELETED);
    }
}
