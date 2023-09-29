package com.example.cargive.domain.tag.controller;

import com.example.cargive.domain.tag.service.TagService;
import com.example.cargive.global.base.BaseResponse;
import com.example.cargive.global.base.BaseResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tag")
public class TagController {
    private final TagService tagService;

    @GetMapping("/{carId}")// 차량 특징 카드 조회
    public ResponseEntity<BaseResponse> getTagList(@PathVariable Long carId) {
        return BaseResponse.toResponseEntityContainsResult(tagService.getTagList(carId));
    }

    @DeleteMapping("/{tagId}") // 차량 특징 카드 삭제
    public ResponseEntity<BaseResponse> deleteTag(@PathVariable Long tagId,
                                                  @RequestParam Long carId) {
        tagService.deleteTag(carId, tagId);
        return BaseResponse.toResponseEntityContainsStatus(BaseResponseStatus.DELETED);
    }
}
