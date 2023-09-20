package com.example.cargive.domain.favorite.controller;

import com.example.cargive.domain.favorite.controller.dto.request.FavoriteGroupEditRequest;
import com.example.cargive.domain.favorite.controller.dto.request.FavoriteGroupRequest;
import com.example.cargive.domain.favorite.infra.query.dto.FavoriteQueryResponse;
import com.example.cargive.domain.favorite.service.FavoritePkGroupService;
import com.example.cargive.global.base.BaseResponse;
import com.example.cargive.global.base.BaseResponseStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/favorite/group")
public class FavoritePkGroupController {
    private final FavoritePkGroupService favoritePkGroupService;

    @GetMapping // 정렬 기준에 따른 즐겨찾기 그룹 조회
    public BaseResponse<FavoriteQueryResponse> getFavoriteGroups(@RequestParam Long memberId,
                                                                 @RequestParam String sortBy,
                                                                 @RequestParam int page) {
        return new BaseResponse<>(favoritePkGroupService.findFavoriteGroups(memberId, sortBy, page));
    }

    @PostMapping // 즐겨찾기 그룹 생성
    public BaseResponse<BaseResponseStatus> createFavoriteGroup(@RequestParam(name = "memberId") Long memberId,
                                                                @RequestBody @Valid FavoriteGroupRequest request) {
        favoritePkGroupService.createFavoriteGroup(memberId, request);
        return new BaseResponse<>(BaseResponseStatus.CREATED);
    }

    @PutMapping("/{favoriteGroupId}") // 즐겨찾기 그룹 수정
    public BaseResponse<BaseResponseStatus> editFavoriteGroup(@RequestParam(name = "memberId") Long memberId,
                                                              @PathVariable Long favoriteGroupId,
                                                              @RequestBody @Valid FavoriteGroupEditRequest editRequest) {
        favoritePkGroupService.editFavoriteGroup(memberId, favoriteGroupId, editRequest);
        return new BaseResponse<>(BaseResponseStatus.SUCCESS);
    }

    @DeleteMapping("/{favoriteGroupId}") // 즐겨찾기 그룹 삭제
    public BaseResponse<BaseResponseStatus> deleteFavoriteGroup(@RequestParam(name = "memberId") Long memberId,
                                                                @PathVariable Long favoriteGroupId) {
        favoritePkGroupService.deleteFavoriteGroup(memberId, favoriteGroupId);
        return new BaseResponse<>(BaseResponseStatus.SUCCESS);
    }
}