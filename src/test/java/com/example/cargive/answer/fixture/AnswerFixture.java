package com.example.cargive.answer.fixture;

import com.example.cargive.domain.answer.entity.Answer;
import com.example.cargive.domain.question.entity.Question;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AnswerFixture {
    ANSWER_1("Content"),
    ANSWER_2("Content2")
    ;

    private final String content;

    public Answer createEntity(Question question) {
        return new Answer(this.content, question);
    }
}
