package com.example.cargive.domain.question.controller;

import com.example.cargive.domain.question.service.QuestionFindService;
import com.example.cargive.global.base.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/question")
public class QuestionFindController {
    private final QuestionFindService questionFindService;

    @GetMapping("/{questionId}")
    public ResponseEntity<BaseResponse> loadQuestion(@RequestParam Long memberId,
                                                     @PathVariable Long questionId) {
        return BaseResponse.toResponseEntityContainsResult(questionFindService.loadQuestion(memberId, questionId));
    }
}
