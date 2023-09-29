package com.example.cargive.favorite.domain;

import com.example.cargive.domain.favorite.entity.FavoritePkGroup;
import com.example.cargive.domain.favorite.entity.FavoritePkInfo;
import com.example.cargive.domain.member.entity.Member;
import com.example.cargive.domain.parkingLot.entity.ParkingLot;
import com.example.cargive.parkinglot.fixture.ParkingLotFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.example.cargive.member.fixture.MemberFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("FavoriteInfo 도메인 테스트")
public class FavoriteInfoTest {
    private FavoritePkInfo favoritePkInfo;
    private FavoritePkGroup favoritePkGroup;
    private ParkingLot parkingLot;
    private Member member;

    @BeforeEach
    public void initTest() {
        member = WIZ.toMember();
        parkingLot = ParkingLotFixture.PARKING_LOT_1.createEntity();
        favoritePkGroup = new FavoritePkGroup("Test Group", member);
        favoritePkInfo = new FavoritePkInfo(favoritePkGroup, parkingLot);
    }

    @Test
    @DisplayName("FavoriteInfo 도메인 생성 테스트")
    public void createEntityTest() {
        assertAll(
                () -> assertThat(favoritePkInfo.getFavoritePkGroup().getName()).isEqualTo(favoritePkGroup.getName()),
                () -> assertThat(favoritePkInfo.getParkingLot().getLongitude()).isEqualTo(parkingLot.getLongitude()),
                () -> assertThat(favoritePkInfo.getParkingLot().getLatitude()).isEqualTo(parkingLot.getLatitude())
        );
    }

    @Test
    @DisplayName("initFavoritePkGroup() 메서드 테스트")
    public void initFavoritePkGroupTest() {
        favoritePkInfo = new FavoritePkInfo(null, parkingLot);

        favoritePkInfo.initFavoritePkGroup(favoritePkGroup);

        assertThat(favoritePkInfo.getFavoritePkGroup().getName()).isEqualTo(favoritePkGroup.getName());
    }
}
