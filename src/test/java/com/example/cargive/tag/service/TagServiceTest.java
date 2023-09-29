package com.example.cargive.tag.service;

import com.example.cargive.common.ServiceTest;
import com.example.cargive.domain.car.entity.Car;
import com.example.cargive.domain.tag.controller.dto.response.TagResponse;
import com.example.cargive.domain.tag.entity.Tag;
import com.example.cargive.domain.tag.infra.dto.TagQueryResponse;
import com.example.cargive.domain.tag.service.TagService;
import com.example.cargive.global.base.BaseException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;

import static com.example.cargive.car.fixture.CarFixture.*;
import static com.example.cargive.tag.fixture.TagFixture.*;
import static org.assertj.core.api.Assertions.*;

@DisplayName("Tag [Service Layer] -> TagService 테스트")
public class TagServiceTest extends ServiceTest {
    @Autowired
    private TagService tagService;

    private Car car;
    private Car errorCar;
    private Tag tag;
    private Long carId;
    private Long otherCarId;
    private Long tagId;

    @BeforeEach
    public void initTest() {
        car = CAR_1.createEntity();
        errorCar = CAR_2.createEntity();

        tag = TAG_1.createEntity();

        car.addTag(tag);

        tag.initCar(car);

        carId = carRepository.save(car).getId();
        otherCarId = carRepository.save(errorCar).getId();
        tagId = tagRepository.save(tag).getId();
    }

    @AfterEach
    public void afterTest() {
        carRepository.deleteAll();
        tagRepository.deleteAll();
    }

    @Nested
    @DisplayName("특정 차량에 등록된 차량 특징 카드를 조회")
    class getTagListTest {
        private final static Long errorCarId = Long.MAX_VALUE;

        @Test
        @DisplayName("데이터를 조회한 결과가 존재하지 않은 경우, 오류를 반환한다")
        public void throwExceptionByEmptyList() {
            // when
            assertThatThrownBy(() -> tagService.getTagList(errorCarId))
                    .isInstanceOf(BaseException.class);
        }

        @Test
        @DisplayName("데이터 조회에 성공한다")
        public void successGetTagList() {
            // when
            TagQueryResponse<TagResponse> tagList = tagService.getTagList(carId);

            // then
            assertThat(tagList.getTagList().size()).isEqualTo(1);
        }
    }

    @Nested
    @DisplayName("특정 차량에 등록된 차량 특징 카드를 삭제")
    class deleteTagTest {
        private final static Long errorCarId = Long.MAX_VALUE;
        private final static Long errorTagId = Long.MAX_VALUE;

        @Test
        @DisplayName("유효하지 않은 carId로 요청하는 경우, 오류를 반환한다")
        public void throwExceptionByInvalidCarId() {
            // when - then
            assertThatThrownBy(() -> tagService.deleteTag(errorCarId, tagId))
                    .isInstanceOf(BaseException.class);
        }

        @Test
        @DisplayName("유효하지 않은 tagId로 요청하는 경우, 오류를 반환한다")
        public void throwExceptionByInvalidTagId() {
            // when - then
            assertThatThrownBy(() -> tagService.deleteTag(carId, errorTagId))
                    .isInstanceOf(BaseException.class);
        }

        @Test
        @DisplayName("차량의 정보와 차량 특징 카드의 정보가 일치하지 않는 경우, 오류를 반환한다")
        public void throwExceptionByInvalidCar() {
            // when - then
            assertThatThrownBy(() -> tagService.deleteTag(otherCarId, tagId))
                    .isInstanceOf(BaseException.class);
        }

        @Test
        @DisplayName("차량 특징 카드의 삭제에 성공한다")
        public void successDeleteTag() {
            // when
            tagService.deleteTag(carId, tagId);

            // then
            assertThat(car.getTagList().isEmpty()).isTrue();
        }
    }
}