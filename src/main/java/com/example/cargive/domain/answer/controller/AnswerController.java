package com.example.cargive.domain.answer.controller;

import com.example.cargive.domain.answer.controller.dto.AnswerRequest;
import com.example.cargive.domain.answer.service.AnswerService;
import com.example.cargive.global.base.BaseResponse;
import com.example.cargive.global.base.BaseResponseStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/answer")
public class AnswerController {
    private final AnswerService answerService;

    @PostMapping("/{questionId}")
    public ResponseEntity<BaseResponse> createAnswer(@PathVariable Long questionId,
                                                     @RequestBody @Valid AnswerRequest request) {
        answerService.createAnswer(request, questionId);
        return BaseResponse.toResponseEntityContainsStatus(BaseResponseStatus.CREATED);
    }

    @PutMapping("/{answerId}")
    public ResponseEntity<BaseResponse> editAnswer(@PathVariable Long answerId,
                                                   @RequestBody @Valid AnswerRequest request) {
        answerService.editAnswer(request, answerId);
        return BaseResponse.toResponseEntityContainsStatus(BaseResponseStatus.SUCCESS);
    }

    @DeleteMapping("/{answerId}")
    public ResponseEntity<BaseResponse> deleteAnswer(@PathVariable Long answerId) {
        answerService.deleteAnswer(answerId);
        return BaseResponse.toResponseEntityContainsStatus(BaseResponseStatus.DELETED);
    }
}