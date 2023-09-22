package com.example.cargive.domain.favorite.controller;

import com.example.cargive.domain.favorite.service.FavoritePkInfoService;
import com.example.cargive.global.base.BaseResponse;
import com.example.cargive.global.base.BaseResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/favorite/info")
public class FavoritePkInfoController {
    private final FavoritePkInfoService favoritePkInfoService;

    @GetMapping("/{favoriteGroupId}") // 즐겨찾기된 주차장 조회
    public ResponseEntity<BaseResponse> getFavoritePkInfos(@PathVariable Long favoriteGroupId,
                                                           @RequestParam String sortBy,
                                                           @RequestParam Long memberId,
                                                           @RequestParam Long cursorId) {
        return BaseResponse.toResponseEntityContainsResult(favoritePkInfoService
                .getFavoriteInfos(memberId, sortBy, favoriteGroupId, cursorId));
    }

    @PostMapping("/{favoriteGroupId}") // 주차장 즐겨찾기 등록
    public ResponseEntity<BaseResponse> createFavoritePkInfo(@RequestParam(name = "memberId") Long memberId,
                                                                 @PathVariable Long favoriteGroupId,
                                                                 @RequestParam(name = "parkingLotId") Long parkingLotId) {
        favoritePkInfoService.createFavoriteInfo(memberId, favoriteGroupId, parkingLotId);
        return BaseResponse.toResponseEntityContainsStatus(BaseResponseStatus.CREATED);
    }

    @DeleteMapping("/{favoriteInfoId}") // 즐겨찾기에서 주차장 삭제
    public ResponseEntity<BaseResponse> deleteFavoritePkInfo(@RequestParam(name = "memberId") Long memberId,
                                                                 @PathVariable Long favoriteInfoId) {
        favoritePkInfoService.deleteFavoriteInfo(memberId, favoriteInfoId);
        return BaseResponse.toResponseEntityContainsStatus(BaseResponseStatus.SUCCESS);
    }
}
