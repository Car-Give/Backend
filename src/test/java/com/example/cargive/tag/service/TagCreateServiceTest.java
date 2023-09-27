package com.example.cargive.tag.service;

import com.example.cargive.common.ServiceTest;
import com.example.cargive.domain.tag.entity.Tag;
import com.example.cargive.domain.tag.service.TagCreateService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static com.example.cargive.tag.fixture.TagFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tag [Service Layer] -> TagCreateService 테스트")
public class TagCreateServiceTest extends ServiceTest {
    @Autowired
    private TagCreateService tagCreateService;

    private final List<String> tagNameList = new ArrayList<>();
    private final List<Tag> tagList = new ArrayList<>();

    @BeforeEach
    public void initTest() {
        tagRepository.deleteAll();

        tagNameList.add(TAG_1.getName());
        tagNameList.add(TAG_2.getName());
        tagNameList.add(TAG_3.getName());

        Tag tag1 = tagRepository.save(TAG_1.createEntity());
        Tag tag2 = tagRepository.save(TAG_2.createEntity());
        Tag tag3 = tagRepository.save(TAG_3.createEntity());

        tagList.add(tag1);
        tagList.add(tag2);
        tagList.add(tag3);
    }

    @AfterEach
    public void afterTest() {
        tagRepository.deleteAll();
    }

    @Test
    @DisplayName("차량 특징 카드 이름 목록을 통해서 Tag List를 생성한다")
    public void createEntityTest() {
        // when
        List<Tag> tagResponseList = tagCreateService.createTagList(tagNameList);

        // then
        assertAll(
                () -> assertThat(tagResponseList.size()).isEqualTo(3)
        );
    }
}
