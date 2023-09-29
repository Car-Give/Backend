package com.example.cargive.car.domain;

import com.example.cargive.domain.car.entity.Car;
import com.example.cargive.domain.tag.entity.Tag;
import com.example.cargive.global.template.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;

import static com.example.cargive.car.fixture.CarFixture.*;
import static com.example.cargive.tag.fixture.TagFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Car 도메인 테스트")
public class CarTest {
    private Car car;
    private Tag tag1;
    private Tag tag2;
    
    @BeforeEach
    public void initTest() {
        car = CAR_1.createEntity();
        tag1 = TAG_1.createEntity();
        tag2 = TAG_2.createEntity();
    }

    @Test
    @DisplayName("Car 도메인 생성 테스트")
    public void createEntityTest() {
        assertAll(
                () -> assertThat(car.getType()).isEqualTo("TestCar1"),
                () -> assertThat(car.getNumber()).isEqualTo("1"),
                () -> assertThat(car.getRecentCheck()).isBeforeOrEqualTo(LocalDate.now()),
                () -> assertThat(car.getMileage()).isEqualTo(1),
                () -> assertThat(car.isFavorite()).isFalse(),
                () -> assertThat(car.getStatus()).isEqualTo(Status.NORMAL),
                () -> assertThat(car.getMember()).isNull()
        );
    }

    @Test
    @DisplayName("deleteEntity() 메서드 테스트")
    public void deleteEntityTest() {
        car.deleteEntity();

        assertThat(car.getStatus()).isEqualTo(Status.EXPIRED);
    }

    @Test
    @DisplayName("favoriteEntity() 메서드 테스트")
    public void favoriteEntity() {
        car.favoriteEntity();

        assertThat(car.isFavorite()).isTrue();
    }

    @Test
    @DisplayName("editInfo() 메서드 테스트")
    public void editInfoTest() {
        car.editInfo(LocalDate.now(), 10L, "TestImage", Arrays.asList(tag1), Arrays.asList(tag2));

        assertAll(
                () -> assertThat(car.getRecentCheck()).isBeforeOrEqualTo(LocalDate.now()),
                () -> assertThat(car.getMileage()).isEqualTo(10L),
                () -> assertThat(car.getImageUrl()).isEqualTo("TestImage"),
                () -> assertThat(car.getTagList().size()).isEqualTo(1L),
                () -> assertThat(car.getTagList().get(0).getName()).isEqualTo(tag1.getName())
        );
    }

    @Test
    @DisplayName("addTag() 메서드 테스트")
    public void addTagTest() {
        car.addTag(tag1);

        assertThat(car.getTagList().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("removeTag() 메서드 테스트")
    public void removeTag() {
        car.addTag(tag1);
        car.removeTag(tag1);

        assertThat(car.getTagList().isEmpty()).isTrue();
    }
}