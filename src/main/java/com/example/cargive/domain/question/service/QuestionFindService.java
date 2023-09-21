package com.example.cargive.domain.question.service;

import com.example.cargive.domain.member.entity.Member;
import com.example.cargive.domain.question.entity.Question;
import com.example.cargive.domain.question.entity.repository.QuestionRepository;
import com.example.cargive.domain.question.service.response.GetListOfQuestionResponse;
import com.example.cargive.domain.question.service.response.LoadQuestionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    public List<GetListOfQuestionResponse> getListOfQuestion(Long memberId, Long questionId) {
        Member member = questionService.findMember(memberId);
        Question question = configPaging(questionId);
        return questionRepository.findPageOrderByCreateAt(member, question.getCreateAt());
    }

    private Question configPaging(Long questionId) {
        if (questionId == -1)
            return Question.builder().build();
        return questionService.findQuestion(questionId);
    }
}
