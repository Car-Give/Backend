package com.example.cargive.domain.tag.service;

import com.example.cargive.domain.car.entity.Car;
import com.example.cargive.domain.car.service.CarFindService;
import com.example.cargive.domain.tag.controller.dto.response.TagResponse;
import com.example.cargive.domain.tag.entity.Tag;
import com.example.cargive.domain.tag.entity.TagRepository;
import com.example.cargive.domain.tag.infra.dto.TagQueryResponse;
import com.example.cargive.global.base.BaseException;
import com.example.cargive.global.base.BaseResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TagService {
    private final TagRepository tagRepository;
    private final CarFindService carFindService;

    @Transactional(readOnly = true) // Car Entity의 PK값을 통하여, 차량에 등록된 특징 카드를 조회하는 메서드
    public TagQueryResponse<TagResponse> getTagList(Long carId) {
        return getTagListByCar(carId);
    }

    @Transactional // 차량에 등록된 특정 카드를 삭제하는 메서드
    public void deleteTag(Long carId, Long tagId) {
        Car findCar = getCar(carId);
        Tag findTag = getTag(tagId);

        checkValidation(findCar, findTag);
        deleteConnection(findCar, findTag);
    }

    // Car Entity의 PK값을 통하여 데이터를 조회하는 메서드
    private Car getCar(Long carId) {
        return carFindService.getCar(carId);
    }

    // Tag Entity의 PK값을 통하여 데이터를 조회하는 메서드
    private Tag getTag(Long tagId) {
        return tagRepository.findById(tagId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.TAG_NOT_FOUND_ERROR));
    }

    // Car Entity에 등록된 Tag Entity가 맞는지 확인하는 메서드
    private void checkValidation(Car car, Tag tag) {
        if(!tag.getCar().getId().equals(car.getId()))
            throw new BaseException(BaseResponseStatus.CAR_NOT_MATCH_ERROR);
    }

    // Entity 사이의 연관 관계를 제거하는 메서드
    private void deleteConnection(Car car, Tag tag) {
        tag.deleteEntity();
        car.removeTag(tag);
    }

    // Car Entity의 PK값을 토대로, 등록된 Tag Entity List를 조회하는 메서드
    private TagQueryResponse<TagResponse> getTagListByCar(Long carId) {
        TagQueryResponse<TagResponse> responseData = tagRepository.findTagsByCar(carId);

        if(responseData.getTagList().isEmpty())
            throw new BaseException(BaseResponseStatus.TAG_LIST_EMPTY_ERROR);

        return responseData;
    }
}