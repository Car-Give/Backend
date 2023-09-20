package com.example.cargive.question.fixture;

import com.example.cargive.domain.question.entity.Category;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum QuestionFixture {
    QUESTION_0("질문0", "질문내용0", Category.COMMON),
    ;

    private final String title;
    private final String content;
    private final Category category;
}
