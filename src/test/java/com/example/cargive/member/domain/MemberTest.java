package com.example.cargive.member.domain;

import com.example.cargive.car.fixture.CarFixture;
import com.example.cargive.domain.car.entity.Car;
import com.example.cargive.domain.favorite.entity.FavoriteCar;
import com.example.cargive.domain.favorite.entity.FavoritePkGroup;
import com.example.cargive.domain.member.entity.Member;
import com.example.cargive.global.template.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.example.cargive.member.fixture.MemberFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Member 도메인 테스트")
public class MemberTest {
    private Member member;
    private Car car;
    private FavoritePkGroup favoritePkGroup;
    private FavoriteCar favoriteCar;

    @BeforeEach
    public void initTest() {
        member = WIZ.toMember();
        car = CarFixture.CAR_1.createEntity();
        favoritePkGroup = new FavoritePkGroup("Test", member);
        favoriteCar = new FavoriteCar(member, car);
    }

    @Test
    @DisplayName("Member 도메인 생성 테스트")
    public void createEntityTest() {
        assertAll(
                () -> assertThat(member.getLoginId()).isEqualTo(WIZ.getLoginId()),
                () -> assertThat(member.getName()).isEqualTo(WIZ.getName()),
                () -> assertThat(member.getPhoneNumber()).isEqualTo(WIZ.getPhoneNumber()),
                () -> assertThat(member.getEmail().getValue()).isEqualTo(WIZ.getEmail()),
                () -> assertThat(member.getSocial()).isEqualTo(WIZ.getSocial()),
                () -> assertThat(member.getImageUrl()).isEqualTo(WIZ.getImageUrl())
        );
    }

    @Test
    @DisplayName("addFavoritePkGroup() 메서드 테스트")
    public void addFavoritePkGroupTest() {
        // when
        member.addFavoritePkGroup(favoritePkGroup);

        // then
        assertThat(member.getFavoritePkGroupList().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("addFavoriteCar() 메서드 테스트")
    public void addFavoriteCarTest() {
        // when
        member.addFavoriteCar(favoriteCar);

        // then
        assertThat(member.getFavoriteCarList().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("addCar() 메서드 테스트")
    public void addCarTest() {
        // when
        member.addCar(car);

        // then
        assertThat(member.getCarList().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("removeCar() 메서드 테스트")
    public void removeCarTest() {
        // when
        member.addCar(car);
        member.removeCar(car);

        // then
        assertThat(member.getCarList().size()).isEqualTo(0);
    }

    @Test
    @DisplayName("removeFavoritePkGroup() 메서드 테스트")
    public void removeFavoritePkGroupTest() {
        // when
        member.addFavoritePkGroup(favoritePkGroup);
        member.removeFavoritePkGroup(favoritePkGroup);

        // then
        assertThat(member.getFavoritePkGroupList().size()).isEqualTo(0);
    }

    @Test
    @DisplayName("removeFavoriteCar() 메서드 테스트")
    public void removeFavoriteCarTest() {
        // when
        member.addFavoriteCar(favoriteCar);
        member.removeFavoriteCar(favoriteCar);

        // then
        assertThat(member.getFavoriteCarList().size()).isEqualTo(0);
    }

    @Test
    @DisplayName("deleteEntity() 메서드 테스트")
    public void deleteEntityTest() {
        // when
        member.deleteEntity();

        // then
        assertThat(member.getStatus()).isEqualTo(Status.EXPIRED);
    }

    @Test
    @DisplayName("editInfo() 메서드 테스트")
    public void editInfoTest() {
        // given
        String editPhoneNumber = "000-1111-2222";
        String editImageUrl = "test";

        // when
        member.editInfo(editPhoneNumber, editImageUrl);

        // then
        assertThat(member.getPhoneNumber()).isEqualTo(editPhoneNumber);
        assertThat(member.getImageUrl()).isEqualTo(editImageUrl);
    }
}