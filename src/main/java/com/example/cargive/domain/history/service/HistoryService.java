package com.example.cargive.domain.history.service;

import com.example.cargive.domain.car.entity.Car;
import com.example.cargive.domain.car.service.CarFindService;
import com.example.cargive.domain.history.controller.dto.HistoryResponse;
import com.example.cargive.domain.history.entity.History;
import com.example.cargive.domain.history.entity.HistoryRepository;
import com.example.cargive.domain.member.entity.Member;
import com.example.cargive.domain.member.repository.MemberRepository;
import com.example.cargive.domain.parkingLot.entity.ParkingLot;
import com.example.cargive.domain.parkingLot.service.ParkingLotFindService;
import com.example.cargive.global.base.BaseException;
import com.example.cargive.global.base.BaseResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HistoryService {
    private final HistoryRepository historyRepository;
    private final ParkingLotFindService parkingLotFindService;
    private final CarFindService carFindService;
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true) // 특정 차량의 이용 기록을 확인하는 메서드
    public List<HistoryResponse> getHistoryList(Long carId, Long memberId) {
        return getHistoryListByCarId(carId, memberId);
    }

    @Transactional // 특정 차량의 이용 기록을 생성하는 메서드
    public void startUseHistory(Long carId, Long parkingLotId, Long memberId) {
        Car findCar = getCar(carId);
        ParkingLot findParkingLot = getParkingLot(parkingLotId);
        Member findMember = getMember(memberId);

        checkValidation(findCar, findMember);

        History history = createHistory(findCar, findParkingLot);

        makeConnection(findCar, history);

        historyRepository.save(history);
    }

    @Transactional // 특정 차량의 이용 기록을 종료하는 메서드
    public void endUseHistory(Long historyId, Long carId, Long memberId) {
        Car findCar = getCar(carId);
        Member findMember = getMember(memberId);
        History findHistory = getHistory(historyId);

        checkValidation(findCar, findMember, findHistory);

        findHistory.editInfo();
    }

    private void checkValidation(Car car, Member member) {
        if(!car.getMember().getName().equals(member.getName()))
            throw new BaseException(BaseResponseStatus.CAR_MEMBER_NOT_MATCH_ERROR);
    }

    private void checkValidation(Car car, Member member, History history) {
        if(!car.getMember().getName().equals(member.getName()))
            throw new BaseException(BaseResponseStatus.CAR_MEMBER_NOT_MATCH_ERROR);

        if(!history.getCar().getId().equals(car.getId()))
            throw new BaseException(BaseResponseStatus.HISTORY_CAR_NOT_MATCH_ERROR);
    }

    private void makeConnection(Car car, History history) {
        car.addHistory(history);
    }

    private List<HistoryResponse> getHistoryListByCarId(Long carId, Long memberId) {
        List<HistoryResponse> responseData = historyRepository.findHistoryListByCarId(carId, memberId);

        if(responseData.isEmpty()) throw new BaseException(BaseResponseStatus.HISTORY_LIST_EMPTY_ERROR);

        return responseData;
    }

    private History createHistory(Car car, ParkingLot parkingLot) {
        return new History(car, parkingLot);
    }

    private History getHistory(Long historyId) {
        return historyRepository.findById(historyId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.HISTORY_NOT_FOUND_ERROR));
    }

    private Car getCar(Long carId) {
        return carFindService.getCar(carId);
    }

    private ParkingLot getParkingLot(Long parkingLotId) {
        return parkingLotFindService.findParkingLotById(parkingLotId);
    }

    private Member getMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.MEMBER_NOT_FOUND_ERROR));
    }
}
