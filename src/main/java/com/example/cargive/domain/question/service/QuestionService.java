package com.example.cargive.domain.question.service;

import com.example.cargive.domain.member.entity.Member;
import com.example.cargive.domain.member.repository.MemberRepository;
import com.example.cargive.domain.question.controller.dto.request.SaveQuestionRequest;
import com.example.cargive.domain.question.entity.Question;
import com.example.cargive.domain.question.entity.repository.QuestionRepository;
import com.example.cargive.global.base.BaseException;
import com.example.cargive.global.base.BaseResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class QuestionService {
    private final MemberRepository memberRepository;
    private final QuestionRepository questionRepository;

    public Long saveQuestion(Long memberId, SaveQuestionRequest saveQuestionRequest) {
        Member member = findMember(memberId);
        return questionRepository.save(saveQuestionRequest.toQuestion(member)).getId();
    }

    public void deleteQuestion(Long memberId, Long questionId) {
        isMatchMemberAndQuestion(findMember(memberId), findQuestion(questionId));
        questionRepository.deleteById(questionId);
    }

    protected Member findMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.MEMBER_NOT_FOUND_ERROR));
    }

    protected Question findQuestion(Long questionId) {
        return questionRepository.findById(questionId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.QUESTION_NOT_FOUND_ERROR));
    }

    protected void isMatchMemberAndQuestion(Member member, Question question) {
        if (!question.getMember().equals(member))
            throw new BaseException(BaseResponseStatus.QUESTION_MEMBER_NOT_MATCH_ERROR);
    }
}
