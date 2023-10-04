package com.example.cargive.domain.car.controller.dto.response;

import com.example.cargive.domain.tag.controller.dto.response.TagResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class CarResponse {
    private Long id; // Car Entity의 PK값
    private String type; // 차량의 종류
    private String number; // 차량의 번호
    private LocalDate recentCheck; // 차량을 마지막으로 수정한 날짜
    private Long mileage; // 차량의 주행거리
    private String imageUrl; // 차량 이미지에 접근할 수 있는 URL
    private boolean isFavorite; // 차량의 즐겨찾기 등록 유무
    private List<TagResponse> tagList = new ArrayList<>(); // 차량에 함께 등록된 차량 특징 카드 목록

    public CarResponse(Long id, String type, String number, LocalDate recentCheck, Long mileage,
                       String imageUrl, boolean isFavorite, List<TagResponse> tagList) {
        this.id = id;
        this.type = type;
        this.number = number;
        this.recentCheck = recentCheck;
        this.mileage = mileage;
        this.imageUrl = imageUrl;
        this.isFavorite = isFavorite;
        this.tagList = tagList;
    }
}