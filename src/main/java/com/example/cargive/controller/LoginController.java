package com.example.cargive.controller;

import com.example.cargive.dto.PostToken;
import com.example.cargive.dto.UserDto;
import com.example.cargive.entity.User;
import com.example.cargive.exception.NotSnsTypeException;
import com.example.cargive.repository.UserRepository;
import com.example.cargive.security.jwt.AccessTokenDto;
import com.example.cargive.security.oauth.OAuthService;
import com.example.cargive.security.oauth.SNSType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Slf4j
public class LoginController {

    private final OAuthService oAuthService;

    private final UserRepository userRepository;

    /**
     * SNS 로그인
     * @param postToken
     * @param providerId
     * @return
     */
    @PostMapping(value = "/auth/login/{providerId}")
    public ResponseEntity<AccessTokenDto> postToken(@RequestBody PostToken postToken, @PathVariable String providerId
    ) {
        if (providerId == null || providerId.equals("") || (!providerId.equals("kakao") && !providerId.equals("naver")
                && !providerId.equals("google"))) {
            throw new NotSnsTypeException("잘못된 회원 타입입니다.");
        }
        SNSType snsType = switch (providerId) {
            case "kakao" :
                yield SNSType.KAKAO;
            case "naver" :
                yield SNSType.NAVER;
            case "google":
                yield SNSType.GOOGLE;
            default:
                throw new IllegalStateException("Unexpected value: " + providerId);
        };
        return ResponseEntity.ok(oAuthService.oAuthGetUserInfo(snsType, postToken));
    }

    /**
     * 회원 정보 조회
     * @param principal
     * @return
     */
    @GetMapping(value = "/me")
    public ResponseEntity<UserDto> getUserInfo(Principal principal) {
        Optional<User> savedUser = userRepository.findByEmail(principal.getName());
        return savedUser.map(user -> ResponseEntity.ok(UserDto.toDto(user)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    /**
     * 예외 처리
     * @param e
     * @return
     */
    @ExceptionHandler(NotSnsTypeException.class)
    public ResponseEntity<String> exceptionHandler(NotSnsTypeException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}
