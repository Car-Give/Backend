package com.example.cargive.domain.question.service;

import com.example.cargive.domain.member.entity.Member;
import com.example.cargive.domain.question.controller.dto.response.LoadQuestionResponse;
import com.example.cargive.domain.question.entity.Question;
import com.example.cargive.domain.question.entity.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class QuestionFindService {
    private final QuestionRepository questionRepository;
    private final QuestionService questionService;

    public LoadQuestionResponse loadQuestion(Long memberId, Long questionId) {
        Member member = questionService.findMember(memberId);
        Question question = questionService.findQuestion(questionId);
        questionService.isMatchMemberAndQuestion(member, question);

        return LoadQuestionResponse.builder()
                .title(question.getTitle())
                .content(question.getContent())
                .categoryValue(question.getCategory().getValue())
                .build();
    }
}
