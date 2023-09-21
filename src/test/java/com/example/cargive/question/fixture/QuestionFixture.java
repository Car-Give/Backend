package com.example.cargive.question.fixture;

import com.example.cargive.domain.question.entity.Category;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public enum QuestionFixture {
    QUESTION_0(0L, "질문0", "질문내용0", Category.COMMON, LocalDateTime.now()),
    QUESTION_1(1L, "질문1", "질문내용1", Category.COMMON, LocalDateTime.now().plusMinutes(1)),
    QUESTION_2(2L, "질문2", "질문내용2", Category.BUG, LocalDateTime.now().plusMinutes(2)),
    QUESTION_3(3L, "질문3", "질문내용3", Category.BUG, LocalDateTime.now().plusMinutes(3)),
    QUESTION_4(4L, "질문4", "질문내용4", Category.SUGGEST, LocalDateTime.now().plusMinutes(4)),
    QUESTION_5(5L, "질문5", "질문내용5", Category.ETC, LocalDateTime.now().plusMinutes(5)),
    ;

    private final Long id;
    private final String title;
    private final String content;
    private final Category category;
    private final LocalDateTime createAt;
}
