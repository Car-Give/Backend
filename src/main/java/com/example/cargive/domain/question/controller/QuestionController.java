package com.example.cargive.domain.question.controller;

import com.example.cargive.domain.question.controller.dto.request.SaveQuestionRequest;
import com.example.cargive.domain.question.service.QuestionService;
import com.example.cargive.global.base.BaseResponse;
import com.example.cargive.global.base.BaseResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/question")
public class QuestionController {
    private final QuestionService questionService;

    @PostMapping
    public ResponseEntity<BaseResponse> saveQuestion(@RequestParam Long memberId,
                                                     @RequestBody SaveQuestionRequest saveQuestionRequest) {
        questionService.saveQuestion(memberId, saveQuestionRequest);
        return BaseResponse.toResponseEntityContainsStatus(BaseResponseStatus.CREATED);
    }

    @DeleteMapping("/{questionId}")
    public ResponseEntity<BaseResponse> deleteQuestion(@RequestParam Long memberId,
                                                       @PathVariable Long questionId) {
        questionService.deleteQuestion(memberId, questionId);
        return BaseResponse.toResponseEntityContainsStatus(BaseResponseStatus.DELETED);
    }
}
