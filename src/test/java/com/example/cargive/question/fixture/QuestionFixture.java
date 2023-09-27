package com.example.cargive.question.fixture;

import com.example.cargive.domain.member.entity.Member;
import com.example.cargive.domain.question.entity.Category;
import com.example.cargive.domain.question.entity.Question;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public enum QuestionFixture {
    QUESTION_0(1L, "질문0", "질문내용0", Category.COMMON, LocalDateTime.now()),
    QUESTION_1(2L, "질문1", "질문내용1", Category.COMMON, LocalDateTime.now().plusMinutes(1)),
    QUESTION_2(3L, "질문2", "질문내용2", Category.BUG, LocalDateTime.now().plusMinutes(2)),
    QUESTION_3(4L, "질문3", "질문내용3", Category.BUG, LocalDateTime.now().plusMinutes(3)),
    QUESTION_4(5L, "질문4", "질문내용4", Category.SUGGEST, LocalDateTime.now().plusMinutes(4)),
    QUESTION_5(6L, "질문5", "질문내용5", Category.ETC, LocalDateTime.now().plusMinutes(5)),
    QUESTION_6(7L, "질문6", "질문내용6", Category.ETC, LocalDateTime.now().plusMinutes(6)),
    ;

    private final Long id;
    private final String title;
    private final String content;
    private final Category category;
    private final LocalDateTime createAt;

    public Question toQuestion(Member member) {
        return Question.builder()
                .title(title)
                .content(content)
                .category(category)
                .member(member)
                .build();
    }
}
