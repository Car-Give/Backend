package com.example.cargive.domain.car.service;

import com.example.cargive.domain.car.controller.dto.request.CarEditRequest;
import com.example.cargive.domain.car.controller.dto.request.CarRequest;
import com.example.cargive.domain.car.controller.dto.response.CarResponse;
import com.example.cargive.domain.car.entity.Car;
import com.example.cargive.domain.car.entity.CarRepository;
import com.example.cargive.domain.member.entity.Member;
import com.example.cargive.domain.member.repository.MemberRepository;
import com.example.cargive.domain.tag.entity.Tag;
import com.example.cargive.domain.tag.service.TagCreateService;
import com.example.cargive.domain.tag.service.TagFindService;
import com.example.cargive.global.base.BaseException;
import com.example.cargive.global.base.BaseResponseStatus;
import com.example.cargive.global.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CarService {
    private final CarRepository carRepository;
    private final TagFindService tagFindService;
    private final TagCreateService tagCreateService;
    private final MemberRepository memberRepository;
    private final S3Service s3Service;
    private static final String S3_DIRECTORY_CAR = "car";

    @Transactional(readOnly = true)
    public List<CarResponse> getCarList(Long memberId) {
        return getCarListByMemberId(memberId);
    }

    @Transactional
    public void createCar(CarRequest carRequest, MultipartFile file, Long memberId) throws IOException {
        String imageUrl = getImageUrl(file);
        Member findMember = getMember(memberId);
        List<Tag> createdTagList = getTagListByName(carRequest.getTagList());

        Car car = createCar(carRequest, imageUrl, findMember);
        makeConnection(createdTagList, car);
        makeConnection(findMember, car);

        carRepository.save(car);
    }

    @Transactional
    public void editCar(CarEditRequest request, MultipartFile file, Long carId, Long memberId) throws IOException {
        Car findCar = getCarById(carId);
        Member findMember = getMember(memberId);

        checkValidation(findCar, findMember);
        editCarInfo(findCar, request, file);
    }

    @Transactional
    public void deleteCar(Long carId, Long memberId) {
        Car findCar = getCarById(carId);
        Member findMember = getMember(memberId);

        checkValidation(findCar, findMember);

        findCar.deleteEntity();
        findMember.removeCar(findCar);
    }

    // S3를 활용해서 이미지의 URL을 반환하는 메서드
    private String getImageUrl(MultipartFile file) throws IOException {
        if(file.isEmpty()) return "";
        return s3Service.upload(file, S3_DIRECTORY_CAR);
    }

    // Car Entity에 등록된 사용자의 정보와 이용자의 정보를 확인하는 메서드
    private void checkValidation(Car car, Member member) {
        if(!car.getMember().getName().equals(member.getName()))
            throw new BaseException(BaseResponseStatus.CAR_MEMBER_NOT_MATCH_ERROR);
    }

    // 사용자로부터 입력 받은 정보를 통해서 Car Entity의 정보를 수정하는 메서드
    private void editCarInfo(Car car, CarEditRequest request, MultipartFile file) throws IOException {
        LocalDate recentCheck = request.getRecentCheck();
        String imageUrl = getImageUrl(file);
        Long mileage = request.getMileage();
        List<Tag> addedTagList = getTagListByName(request.getAddedTagList());
        List<Tag> deletedTagList = findTagListById(request.getDeletedTagList());

        makeConnection(addedTagList, car);

        car.editInfo(recentCheck, mileage, imageUrl, addedTagList, deletedTagList);
    }

    // 사용자가 입력한 정보를 통해서 Car Entity를 생성하는 메서드
    private Car createCar(CarRequest request, String imageUrl, Member member) {
        return new Car(request.getType(), request.getNumber(),
                request.getRecentCheck(), request.getMileage(), imageUrl, member);
    }

    // 생성된 Car Entity와 Tag List의 연관 관계를 맺는 메서드
    private void makeConnection(List<Tag> tagList, Car car) {
        tagList.forEach(tag -> {
            car.addTag(tag);
            tag.initCar(car);
        });
    }

    private void makeConnection(Member member, Car car) {
        member.addCar(car);
        car.initMember(member);
    }

    // 사용자가 등록한 자동차 정보를 조회하는 메서드
    private List<CarResponse> getCarListByMemberId(Long memberId) {
        List<CarResponse> responseData = carRepository.findCarByMemberId(memberId);

        if(responseData.isEmpty()) throw new BaseException(BaseResponseStatus.CAR_LIST_EMPTY_ERROR);

        return responseData;
    }

    // 사용자로부터 입력 받은 TagName을 통하여 Tag Entity List를 생성하는 메서드
    private List<Tag> getTagListByName(List<String> tagNameList) {
        if(tagNameList.isEmpty()) return new ArrayList<>();
        return tagCreateService.createTagList(tagNameList);
    }

    // 사용자로부터 입력 받은 TagId를 통하여 Tag Entity List를 조회하는 메서드
    private List<Tag> findTagListById(List<Long> tagIdList) {
        if(tagIdList.isEmpty()) return new ArrayList<>();
        return tagFindService.findTagListById(tagIdList);
    }

    // PK값을 통하여 Car Entity를 조회하는 메서드
    private Car getCarById(Long carId) {
        return carRepository.findById(carId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.CAR_NOT_FOUND_ERROR));
    }

    // PK값을 통하여 Member Entity를 조회하는 메서드
    private Member getMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.MEMBER_NOT_FOUND_ERROR));
    }
}
