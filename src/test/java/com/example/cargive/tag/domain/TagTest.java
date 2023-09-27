package com.example.cargive.tag.domain;

import com.example.cargive.domain.car.entity.Car;
import com.example.cargive.domain.tag.entity.Tag;
import com.example.cargive.global.template.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.example.cargive.car.fixture.CarFixture.*;
import static com.example.cargive.tag.fixture.TagFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("Tag 도메인 테스트")
public class TagTest {
    private Tag tag;
    private Car car;

    @BeforeEach
    public void initTest() {
        tag = TAG_1.createEntity();
        car = CAR_1.createEntity();
    }

    @Test
    @DisplayName("Tag 도메인 생성 테스트")
    public void createEntityTest() {
        assertAll(
                () -> assertThat(tag.getName()).isEqualTo("태그1"),
                () -> assertThat(tag.getStatus()).isEqualTo(Status.NORMAL),
                () -> assertThat(tag.getCar()).isNull()
        );
    }

    @Test
    @DisplayName("initCar() 메서드 테스트")
    public void initCarTest() {
        tag.initCar(car);

        assertThat(tag.getCar().getType()).isEqualTo(car.getType());
    }

    @Test
    @DisplayName("deleteEntity() 메서드 테스트")
    public void deleteEntityTest() {
        tag.deleteEntity();

        assertThat(tag.getStatus()).isEqualTo(Status.EXPIRED);
    }
}
