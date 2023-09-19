package com.example.cargive.domain.favorite.service;

import com.example.cargive.domain.favorite.controller.dto.response.FavoritePkInfoResponse;
import com.example.cargive.domain.favorite.entity.*;
import com.example.cargive.domain.favorite.entity.repository.FavoritePkInfoRepository;
import com.example.cargive.domain.favorite.entity.sortCondition.FavoriteInfoSortCondition;
import com.example.cargive.domain.favorite.infra.query.dto.FavoriteQueryResponse;
import com.example.cargive.domain.member.entity.Member;
import com.example.cargive.domain.parkingLot.entity.ParkingLot;
import com.example.cargive.domain.parkingLot.service.ParkingLotFindService;
import com.example.cargive.global.base.BaseResponseStatus;
import com.example.cargive.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FavoritePkInfoService {
    private final FavoritePkGroupFindService favoritePkGroupFindService;
    private final FavoritePkInfoRepository favoritePkInfoRepository;
    private final ParkingLotFindService parkingLotFindService;

    // 특정 즐겨찾기 그룹에 속한 주차장 데이터를 조회
    @Transactional(readOnly = true)
    public FavoriteQueryResponse<FavoritePkInfoResponse> getFavoriteInfos(Long memberId, String sortBy,
                                                                          Long favoriteGroupId, int page) {
        FavoriteInfoSortCondition sortCondition = FavoriteInfoSortCondition.from(sortBy);
        FavoriteQueryResponse<FavoritePkInfoResponse> responseData =
                getQueryResponse(sortCondition, memberId, favoriteGroupId, page);

        if(responseData.getFavoriteList().isEmpty())
            throw new BusinessException(BaseResponseStatus.FAVORITE_NOT_FOUND_ERROR);

        return responseData;
    }

    // 주차장을 특정 즐겨찾기 그룹에 등록
    @Transactional
    public void createFavoriteInfo(Long memberId, Long favoriteGroupId, Long parkingLotId) {
        Member findMember = getMember(memberId);
        FavoritePkGroup findPkGroup = getFavoriteGroup(findMember, favoriteGroupId);
        ParkingLot findPkLot = getParkingLot(parkingLotId);

        checkPkLotAndGroupValidation(findPkLot, findPkGroup);

        FavoritePkInfo favoriteInfo = getFavoriteInfo(findPkGroup, findPkLot);
        favoritePkInfoRepository.save(favoriteInfo);
    }

    // 즐겨찾기 그룹에 속한 주차장 정보를 삭제
    @Transactional
    public void deleteFavoriteInfo(Long memberId, Long favoriteInfoId) {
        FavoritePkInfo findValue = getFavoriteInfoById(favoriteInfoId);
        Member findMember = getMember(memberId);

        checkMemberValidation(findMember, findValue);

        deleteConnection(findValue);
        favoritePkInfoRepository.delete(findValue);
    }

    // 정렬 기준에 따라서 다른 결과를 반환하는 메서드
    private FavoriteQueryResponse<FavoritePkInfoResponse> getQueryResponse(FavoriteInfoSortCondition sortCondition,
                                                                           Long memberId, Long favoriteGroupId,
                                                                           int page) {
        if(sortCondition.equals(FavoriteInfoSortCondition.TIME))
            return favoritePkInfoRepository.getMyFavoritePkInfo(memberId, favoriteGroupId, page);
        throw new BusinessException(BaseResponseStatus.INPUT_INVALID_VALUE);
    }

    // 현재 로그인하고있는 사용자의 정보와 즐겨찾기 그룹을 소유하고 있는 사용자의 정보를 비교
    private void checkMemberValidation(Member member, FavoritePkInfo findValue) {
        if(!findValue.getFavoritePkGroup().getMember().getName().equals(member.getName()))
            throw new BusinessException(BaseResponseStatus.INPUT_INVALID_VALUE);
    }

    // 데이터의 상태를 조회하는 메소드, 삭제된 데이터인 경우에는 오류 반환
    private void checkPkLotAndGroupValidation(ParkingLot parkingLot, FavoritePkGroup favoritePkGroup) {
        if(parkingLot.getStatus().getValue().equals("소멸") || favoritePkGroup.getStatus().getValue().equals("소멸"))
            throw new BusinessException(BaseResponseStatus.INPUT_INVALID_VALUE);
    }

    // 데이터 연관 관계를 제거하는 메소드
    private void deleteConnection(FavoritePkInfo favoritePkInfo) {
        favoritePkInfo.getFavoritePkGroup().deleteFromList(favoritePkInfo);
    }

    // 별도의 사용자 조회 메서드가 구현되어 있지 않기 때문에 임시로 구현, 추후에 변동 예정
    private Member getMember(Long memberId) {
        return null;
    }

    // 생성자를 통해서 Entity를 생성한 뒤, 반환
    private FavoritePkInfo getFavoriteInfo(FavoritePkGroup favoritePkGroup, ParkingLot parkingLot) {
        FavoritePkInfo favoritePkInfo = FavoritePkInfo.builder()
                                                      .favoritePkGroup(favoritePkGroup)
                                                      .parkingLot(parkingLot)
                                                      .build();

        favoritePkGroup.addFavoritePkInfo(favoritePkInfo);

        return favoritePkInfo;
    }

    // Entity의 PK를 통하여 데이터 조회
    private FavoritePkInfo getFavoriteInfoById(Long favoriteInfoId) {
        return favoritePkInfoRepository.findById(favoriteInfoId)
                .orElseThrow(() -> new BusinessException(BaseResponseStatus.FAVORITE_NOT_FOUND_ERROR));
    }

    // Entity의 PK를 통하여 데이터 조회
    private FavoritePkGroup getFavoriteGroup(Member member, Long favoriteGroupId) {
        return favoritePkGroupFindService.findFavoriteById(member, favoriteGroupId);
    }

    // Entity의 PK를 통하여 데이터 조회
    private ParkingLot getParkingLot(Long parkingLotId) {
        return parkingLotFindService.findParkingLotById(parkingLotId);
    }
}