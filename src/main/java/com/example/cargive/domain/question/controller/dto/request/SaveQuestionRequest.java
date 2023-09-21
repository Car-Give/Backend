package com.example.cargive.domain.question.controller.dto.request;

import com.example.cargive.domain.member.entity.Member;
import com.example.cargive.domain.question.entity.Category;
import com.example.cargive.domain.question.entity.Question;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SaveQuestionRequest(
        @NotBlank(message = "제목은 필수입니다.")
        @Size(max = 24, message = "제목은 최대 24자까지 입력 가능합니다.")
        String title,

        @NotBlank(message = "내용은 필수입니다.")
        @Size(max = 500, message = "내용은 최대 500자까지 입력 가능합니다.")
        String content,

        @NotBlank(message = "카테고리는 필수입니다.")
        String categoryValue
) {
        public Question toQuestion(Member member) {
            return Question.builder()
                    .title(title)
                    .content(content)
                    .category(Category.from(categoryValue))
                    .member(member)
                    .build();
        }
}
