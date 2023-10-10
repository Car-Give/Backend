package com.example.cargive.domain.favorite.service;

import com.example.cargive.domain.car.entity.Car;
import com.example.cargive.domain.car.service.CarFindService;
import com.example.cargive.domain.favorite.entity.Favorite;
import com.example.cargive.domain.favorite.entity.FavoriteCar;
import com.example.cargive.domain.favorite.entity.FavoritePkGroup;
import com.example.cargive.domain.favorite.entity.repository.FavoriteRepository;
import com.example.cargive.domain.member.entity.Member;
import com.example.cargive.domain.member.repository.MemberRepository;
import com.example.cargive.global.base.BaseException;
import com.example.cargive.global.base.BaseResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FavoriteCarService {
    private final FavoriteRepository favoriteRepository;
    private final CarFindService carFindService;
    private final MemberRepository memberRepository;

    @Transactional // 차량을 즐겨찾기에 등록
    public void createFavoriteCar(Long memberId, Long carId) {
        Member findMember = getMember(memberId);
        Car findCar = getCar(carId);

        checkValidation(findMember, findCar);

        FavoriteCar favoriteCarEntity = createFavoriteCarEntity(findMember, findCar);

        makeConnection(favoriteCarEntity, findCar, findMember);

        favoriteRepository.save(favoriteCarEntity);
    }

    @Transactional // 차량을 즐겨찾기에서 해제
    public void deleteFavoriteCar(Long memberId, Long favoriteCarId) {
        FavoriteCar findFavoriteCar = getFavoriteCarById(favoriteCarId);
        Member findMember = getMember(memberId);

        checkValidation(findMember, findFavoriteCar);

        findFavoriteCar.deleteEntity();
        findMember.removeFavoriteCar(findFavoriteCar);
    }

    // 차량을 동록한 사람과 사용자의 일치 여부를 확인하는 메서드
    private void checkValidation(Member member, Car car) {
        if(!car.getMember().getName().equals(member.getName()))
            throw new BaseException(BaseResponseStatus.CAR_MEMBER_NOT_MATCH_ERROR);
    }

    // 이용자의 정보와 등록자의 정보가 일치하는지를 확인하는 메서드
    private void checkValidation(Member member, FavoriteCar favoriteCar) {
        if(!favoriteCar.getMember().getName().equals(member.getName()))
            throw new BaseException(BaseResponseStatus.FAVORITE_MEMBER_NOT_MATCH_ERROR);
    }

    // Entity 사이의 연관 관계를 맺는 메서드
    private void makeConnection(FavoriteCar favoriteCar, Car car, Member member) {
        car.favoriteEntity();
        member.addFavoriteCar(favoriteCar);
    }

    // Member Entity, Car Entity를 통하여 새로운 객체를 반환하는 메서드
    private FavoriteCar createFavoriteCarEntity(Member member, Car car) {
        return new FavoriteCar(member, car);
    }

    // Entity의 PK값을 통하여 특정 데이터를 조회하는 메서드
    private FavoriteCar getFavoriteCarById(Long favoriteCarId) {
        Favorite findFavorite = favoriteRepository.findById(favoriteCarId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.FAVORITE_NOT_FOUND_ERROR));

        if(findFavorite instanceof FavoritePkGroup)
            throw new BaseException(BaseResponseStatus.FAVORITE_TYPE_ERROR);

        return (FavoriteCar) findFavorite;
    }

    // Member Entity의 PK값을 통하여 데이터를 조회하는 메서드
    private Member getMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.MEMBER_NOT_FOUND_ERROR));
    }

    // Car Entity의 PK값을 통하여 데이터를 조회하는 메서드
    private Car getCar(Long carId) {
        return carFindService.getCar(carId);
    }
}
