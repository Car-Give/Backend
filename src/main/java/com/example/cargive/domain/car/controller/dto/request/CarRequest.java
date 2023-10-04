package com.example.cargive.domain.car.controller.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
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
public class CarRequest {
    @NotBlank(message = "차량을 등록하기 위한 차종을 입력해주세요")
    private String type; // 차종

    @NotBlank(message = "차량을 등록하기 위한 차 번호를 입력해주세요")
    private String number; // 차 번호

    @NotNull(message = "차량을 등록하기 위한 마지막 점검 일자를 입력해주세요")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate recentCheck; // 마지막 점검 일자

    @NotNull(message = "차량을 등록하기 위한 주행 거리를 입력해주세요")
    private Long mileage; // 주행거리

    private List<String> tagList = new ArrayList<>(); // 차량 특징 카드 PK 리스트
}
