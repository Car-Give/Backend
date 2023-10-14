package com.example.cargive.domain.car.controller;

import com.example.cargive.domain.car.controller.dto.request.CarEditRequest;
import com.example.cargive.domain.car.controller.dto.request.CarRequest;
import com.example.cargive.domain.car.service.CarService;
import com.example.cargive.global.base.BaseResponse;
import com.example.cargive.global.base.BaseResponseStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/car")
public class CarController {
    private final CarService carService;

    @GetMapping // 자신이 등록한 자동차를 조회
    public ResponseEntity<BaseResponse> getCarList(@RequestParam Long memberId) {
        return BaseResponse.toResponseEntityContainsResult(carService.getCarList(memberId));
    }

    @PostMapping // 자동차 등록
    public ResponseEntity<BaseResponse> createCar(@RequestPart(value = "request") @Valid CarRequest request,
                                                  @RequestPart(value = "file") MultipartFile file,
                                                  @RequestParam Long memberId) throws IOException {
        carService.createCar(request, file, memberId);
        return BaseResponse.toResponseEntityContainsStatus(BaseResponseStatus.CREATED);
    }

    @PutMapping("/{carId}") // 자동차 정보 수정
    public ResponseEntity<BaseResponse> editCar(@RequestPart(value = "request") @Valid CarEditRequest request,
                                                @RequestPart(value = "file", required = false) MultipartFile file,
                                                @PathVariable Long carId,
                                                @RequestParam Long memberId) throws IOException {
        carService.editCar(request, file, carId, memberId);
        return BaseResponse.toResponseEntityContainsStatus(BaseResponseStatus.SUCCESS);
    }

    @DeleteMapping("/{carId}") // 자동차 정보 삭제
    public ResponseEntity<BaseResponse> deleteCar(@PathVariable Long carId,
                                                  @RequestParam Long memberId) {
        carService.deleteCar(carId, memberId);
        return BaseResponse.toResponseEntityContainsStatus(BaseResponseStatus.DELETED);
    }
}
