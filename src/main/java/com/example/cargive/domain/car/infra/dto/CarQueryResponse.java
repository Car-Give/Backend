package com.example.cargive.domain.car.infra.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class CarQueryResponse<T> {
    private List<T> carList = new ArrayList<>();

    public CarQueryResponse(List<T> carList) {
        this.carList = carList;
    }
}
