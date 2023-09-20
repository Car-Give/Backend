package com.example.cargive.domain.favorite.controller;

import com.example.cargive.domain.favorite.infra.query.dto.FavoriteQueryResponse;
import com.example.cargive.domain.favorite.service.FavoritePkInfoService;
import com.example.cargive.global.base.BaseResponse;
import com.example.cargive.global.base.BaseResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/favorite/info")
public class FavoritePkInfoController {
    private final FavoritePkInfoService favoritePkInfoService;

    @GetMapping("/{favoriteGroupId}") // 즐겨찾기된 주차장 조회
    public BaseResponse<FavoriteQueryResponse> getFavoritePkInfos(@PathVariable Long favoriteGroupId,
                                                                  @RequestParam String sortBy,
                                                                  @RequestParam Long memberId,
                                                                  @RequestParam Long cursorId) {
        return new BaseResponse<>(favoritePkInfoService
                .getFavoriteInfos(memberId, sortBy, favoriteGroupId, cursorId));
    }

    @PostMapping("/{favoriteGroupId}") // 주차장 즐겨찾기 등록
    public BaseResponse<BaseResponseStatus> createFavoritePkInfo(@RequestParam(name = "memberId") Long memberId,
                                                                 @PathVariable Long favoriteGroupId,
                                                                 @RequestParam(name = "parkingLotId") Long parkingLotId) {
        favoritePkInfoService.createFavoriteInfo(memberId, favoriteGroupId, parkingLotId);
        return new BaseResponse<>(BaseResponseStatus.CREATED);
    }

    @DeleteMapping("/{favoriteInfoId}") // 즐겨찾기에서 주차장 삭제
    public BaseResponse<BaseResponseStatus> deleteFavoritePkInfo(@RequestParam(name = "memberId") Long memberId,
                                                                 @PathVariable Long favoriteInfoId) {
        favoritePkInfoService.deleteFavoriteInfo(memberId, favoriteInfoId);
        return new BaseResponse<>(BaseResponseStatus.SUCCESS);
    }
}
