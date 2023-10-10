package com.example.cargive.domain.favorite.service;

import com.example.cargive.domain.favorite.controller.dto.request.FavoriteGroupEditRequest;
import com.example.cargive.domain.favorite.controller.dto.request.FavoriteGroupRequest;
import com.example.cargive.domain.favorite.controller.dto.response.FavoritePkGroupResponse;
import com.example.cargive.domain.favorite.entity.*;
import com.example.cargive.domain.favorite.entity.repository.FavoriteRepository;
import com.example.cargive.domain.favorite.entity.sortCondition.FavoriteGroupSortCondition;
import com.example.cargive.domain.favorite.infra.query.dto.FavoriteQueryResponse;
import com.example.cargive.domain.member.entity.Member;
import com.example.cargive.domain.member.repository.MemberRepository;
import com.example.cargive.global.base.BaseException;
import com.example.cargive.global.base.BaseResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FavoritePkGroupService {
    private final FavoriteRepository favoritesRepository;
    private final MemberRepository memberRepository;

    // 정렬 기준을 통해서 즐겨찾기 그룹들을 조회
    @Transactional(readOnly = true)
    public FavoriteQueryResponse<FavoritePkGroupResponse> findFavoriteGroups(Long memberId, String sortBy,
                                                                             Long cursorId) {
        FavoriteGroupSortCondition sortCondition = FavoriteGroupSortCondition.from(sortBy);

        return getQueryResponse(sortCondition, memberId, cursorId);
    }

    // 주차장 즐겨찾기 그룹 생성
    @Transactional
    public void createFavoriteGroup(Long memberId, FavoriteGroupRequest request) {
        Member findMember = getMember(memberId);
        FavoritePkGroup favoriteGroup = request.toFavoriteGroup(findMember);

        findMember.addFavoritePkGroup(favoriteGroup);

        favoritesRepository.save(favoriteGroup);
    }

    // 즐겨찾기 그룹 이름을 수정
    @Transactional
    public void editFavoriteGroup(Long memberId, Long favoriteGroupId, FavoriteGroupEditRequest request) {
        Member findMember = getMember(memberId);
        Favorite favoriteGroup = getFavoriteGroup(favoriteGroupId);

        checkMemberValidation(findMember, favoriteGroup);

        editFavoriteGroupName(favoriteGroup, request);
    }

    // 즐겨찾기 그룹 삭제
    @Transactional
    public void deleteFavoriteGroup(Long memberId, Long favoriteGroupId) {
        Member findMember = getMember(memberId);
        Favorite favoriteGroup = getFavoriteGroup(favoriteGroupId);

        checkMemberValidation(findMember, favoriteGroup);

        deleteConnection(favoriteGroup, findMember);
    }

    // 정렬 기준에 따라서 데이터를 조회하는 메서드
    private FavoriteQueryResponse<FavoritePkGroupResponse> getQueryResponse(FavoriteGroupSortCondition sortCondition,
                                                                            Long memberId, Long cursorId) {
        FavoriteQueryResponse<FavoritePkGroupResponse> responseData =
                favoritesRepository.getMyFavoritePkGroup(sortCondition, memberId, cursorId);

        if(!responseData.getFavoriteList().isEmpty()) return responseData;
        throw new BaseException(BaseResponseStatus.FAVORITE_NOT_FOUND_ERROR);
    }

    // 즐겨찾기 그룹의 이름을 변경하는 메소드
    private void editFavoriteGroupName(Favorite favorite, FavoriteGroupEditRequest request) {
        if(favorite instanceof FavoriteCar) throw new BaseException(BaseResponseStatus.INPUT_INVALID_VALUE);
        ((FavoritePkGroup)favorite).changeGroupName(request.name());
    }

    // 그룹을 생성한 사용자와 현재 이용중인 사용자의 정보를 비교하여 일치하지 않을 경우 오류를 반환하는 메소드
    private void checkMemberValidation(Member loginMember, Favorite favoritePkGroup) {
        Member member = favoritePkGroup.getMember();

        if(!loginMember.getLoginId().equals(member.getLoginId()))
            throw new BaseException(BaseResponseStatus.INPUT_INVALID_VALUE);
    }

    // FavoritePkGroup Entity와 Member Entity의 연관 관계를 제거하는 메서드
    private void deleteConnection(Favorite favorite, Member member) {
        favorite.deleteEntity();
        member.removeFavoritePkGroup((FavoritePkGroup) favorite);
    }

    // 별도의 MemberService가 구현되어 있지 않기 때문에 임시 메소드 형성, 추후에 변경 예정
    private Member getMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.MEMBER_NOT_FOUND_ERROR));
    }

    // PK값을 통해서 Favorite Entity를 가져오는 메소드
    private Favorite getFavoriteGroup(Long favoriteGroupId) {
        return favoritesRepository.findById(favoriteGroupId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.FAVORITE_NOT_FOUND_ERROR));
    }
}