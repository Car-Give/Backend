package com.example.cargive.answer.domain;

import com.example.cargive.domain.answer.entity.Answer;
import com.example.cargive.global.template.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.example.cargive.answer.fixture.AnswerFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Answer 도메인 테스트")
public class AnswerTest {
    private Answer answer;

    @BeforeEach
    public void initTest() {
        answer = ANSWER_1.createEntity(null);
    }

    @Test
    @DisplayName("Answer 도메인 생성 테스트")
    public void createEntityTest() {
        assertAll(
                () -> assertThat(answer.getContent()).isEqualTo(ANSWER_1.getContent()),
                () -> assertThat(answer.getStatus()).isEqualTo(Status.NORMAL)
        );
    }

    @Test
    @DisplayName("editInfo() 메서드 테스트")
    public void editInfoTest() {
        // when
        answer.editInfo(ANSWER_2.getContent());

        // then
        assertThat(answer.getContent()).isEqualTo(ANSWER_2.getContent());
    }

    @Test
    @DisplayName("deleteEntity() 메서드 테스트")
    public void deleteEntityTest() {
        // when
        answer.deleteEntity();

        // then
        assertThat(answer.getStatus()).isEqualTo(Status.EXPIRED);
    }
}
