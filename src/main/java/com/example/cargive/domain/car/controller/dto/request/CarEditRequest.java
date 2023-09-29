package com.example.cargive.domain.car.controller.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CarEditRequest {
    @NotNull(message = "수정할 차량의 미지막 점검 일자를 입력해주세요")
    private LocalDate recentCheck; // 마지막 점검 날짜

    @NotNull(message = "수정할 차량의 주행 거리를 입력해주세요")
    private Long mileage; // 주행 거리

    private List<String> addedTagList = new ArrayList<>(); // 새롭게 추가할 차량 특징 카드 PK 리스트

    private List<Long> deletedTagList = new ArrayList<>(); // 삭제할 차량 특징 카드 PK 리스트
}
