package com.example.cargive.tag.service;

import com.example.cargive.common.ServiceTest;
import com.example.cargive.domain.tag.entity.Tag;
import com.example.cargive.domain.tag.service.TagFindService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static com.example.cargive.tag.fixture.TagFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tag [Service Layer] -> TagFindService 테스트")
public class TagFindServiceTest extends ServiceTest {
    @Autowired
    private TagFindService tagFindService;

    private final List<Long> tagIdList = new ArrayList<>();
    private final List<Tag> tagList = new ArrayList<>();

    @BeforeEach
    public void initTest() {
        tagRepository.deleteAll();

        Tag tag1 = tagRepository.save(TAG_1.createEntity());
        Tag tag2 = tagRepository.save(TAG_2.createEntity());
        Tag tag3 = tagRepository.save(TAG_3.createEntity());

        tagList.add(tag1);
        tagIdList.add(tag1.getId());
        tagList.add(tag2);
        tagIdList.add(tag2.getId());
        tagList.add(tag3);
        tagIdList.add(tag3.getId());
    }

    @AfterEach
    public void afterTest() {
        tagRepository.deleteAll();
    }

    @Test
    @DisplayName("Tag의 PK List를 통하여 데이터 List를 조회한다")
    public void findTagListByIdTest() {
        // when
        List<Tag> tagList = tagFindService.findTagListById(tagIdList);

        //then
        assertAll(
                () -> assertThat(tagList.size()).isEqualTo(3)
        );
    }
}
