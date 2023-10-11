package com.example.cargive.domain.answer.service;

import com.example.cargive.domain.answer.entity.Answer;
import com.example.cargive.domain.answer.controller.dto.AnswerRequest;
import com.example.cargive.domain.answer.entity.repository.AnswerRepository;
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
public class AnswerService {
    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;

    public void createAnswer(AnswerRequest request, Long questionId) {
        Question findQuestion = getQuestion(questionId);
        Answer answer = createAnswer(request, findQuestion);

        answerRepository.save(answer);
    }

    public void editAnswer(AnswerRequest request, Long answerId) {
        Answer findAnswer = getAnswer(answerId);
        findAnswer.editInfo(request.content());
    }

    public void deleteAnswer(Long answerId) {
        Answer findAnswer = getAnswer(answerId);
        findAnswer.deleteEntity();
    }

    private Answer createAnswer(AnswerRequest request, Question question) {
        return new Answer(request.content(), question);
    }

    private Question getQuestion(Long questionId) {
        return questionRepository.findById(questionId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.QUESTION_NOT_FOUND_ERROR));
    }

    private Answer getAnswer(Long answerId) {
        return answerRepository.findById(answerId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.ANSWER_NOT_FOUND_ERROR));
    }
}
