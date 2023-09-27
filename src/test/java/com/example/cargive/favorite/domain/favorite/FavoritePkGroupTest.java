package com.example.cargive.favorite.domain.favorite;

import com.example.cargive.domain.favorite.entity.FavoritePkGroup;
import com.example.cargive.domain.favorite.entity.FavoritePkInfo;
import com.example.cargive.domain.member.entity.Member;
import com.example.cargive.domain.parkingLot.entity.ParkingLot;
import com.example.cargive.global.template.Status;
import com.example.cargive.member.fixture.MemberFixture;
import com.example.cargive.parkinglot.fixture.ParkingLotFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("FavoritePkGroup 도메인 테스트")
public class FavoritePkGroupTest {
    private Member member;
    private ParkingLot parkingLot;
    private FavoritePkInfo favoritePkInfo;
    private FavoritePkGroup favoritePkGroup;

    @BeforeEach
    public void initTest() {
        member = MemberFixture.MEMBER_1.createEntity();
        parkingLot = ParkingLotFixture.PARKING_LOT_1.createEntity();
        favoritePkGroup = new FavoritePkGroup("Test Group", member);
        favoritePkInfo = new FavoritePkInfo(favoritePkGroup, parkingLot);
    }

    @Test
    @DisplayName("FavoritePkGroup 도메인 생성 테스트")
    public void createEntityTest() {
        assertAll(
                () -> assertThat(favoritePkGroup.getName()).isEqualTo("Test Group"),
                () -> assertThat(favoritePkGroup.getMember().getName()).isEqualTo(member.getName()),
                () -> assertThat(favoritePkGroup.getStatus()).isEqualTo(Status.NORMAL),
                () -> assertThat(favoritePkGroup.getInfoList().isEmpty()).isTrue()
        );
    }

    @Test
    @DisplayName("addFavoritePkInfo() 메서드 테스트")
    public void addFavoritePkInfoTest() {
        favoritePkGroup.addFavoritePkInfo(favoritePkInfo);

        assertThat(favoritePkGroup.getInfoList().size()).isEqualTo(1L);
    }

    @Test
    @DisplayName("deleteFromList() 메서드 테스트")
    public void deleteFromListTest() {
        favoritePkGroup.addFavoritePkInfo(favoritePkInfo);
        favoritePkGroup.deleteFromList(favoritePkInfo);

        assertThat(favoritePkGroup.getInfoList().isEmpty()).isTrue();
    }

    @Test
    @DisplayName("changeGroupName() 메서드 테스트")
    public void changeGroupNameTest() {
        favoritePkGroup.changeGroupName("changedName");

        assertThat(favoritePkGroup.getName()).isEqualTo("changedName");
    }
}