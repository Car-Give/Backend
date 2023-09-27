package com.example.cargive.favorite.domain.favorite;

import com.example.cargive.car.fixture.CarFixture;
import com.example.cargive.domain.car.entity.Car;
import com.example.cargive.domain.favorite.entity.FavoriteCar;
import com.example.cargive.domain.member.entity.Member;
import com.example.cargive.global.template.Status;
import com.example.cargive.member.fixture.MemberFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("FavoriteCar 도메인 테스트")
public class FavoriteCarTest {
    private Member member;
    private Car car;
    private FavoriteCar favoriteCar;

    @BeforeEach
    public void initTest() {
        member = MemberFixture.MEMBER_1.createEntity();
        car = CarFixture.CAR_1.createEntity();
        favoriteCar = new FavoriteCar(member, car);
    }

    @Test
    @DisplayName("FavoriteCar 도메인 생성 테스트")
    public void createEntity() {
        assertAll(
                () -> assertThat(favoriteCar.getMember().getName()).isEqualTo(member.getName()),
                () -> assertThat(favoriteCar.getCar().getType()).isEqualTo(car.getType()),
                () -> assertThat(favoriteCar.getStatus()).isEqualTo(Status.NORMAL)
        );
    }

    @Test
    @DisplayName("deleteEntity() 메서드 테스트")
    public void deleteEntityTest() {
        favoriteCar.deleteEntity();

        assertThat(favoriteCar.getStatus()).isEqualTo(Status.EXPIRED);
    }
}