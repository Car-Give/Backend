package com.example.cargive.global.infra;

import com.example.cargive.global.base.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/health")
public class ElasticBeanstalkHealthCheck {
    @GetMapping
    public ResponseEntity<BaseResponse> getHealthCheck() {
        return BaseResponse.toResponseEntityContainsResult("It Works!");
    }
}
