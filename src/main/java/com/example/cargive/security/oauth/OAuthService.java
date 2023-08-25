package com.example.cargive.security.oauth;

import com.example.cargive.dto.PostToken;
import com.example.cargive.entity.User;
import com.example.cargive.repository.UserRepository;
import com.example.cargive.security.jwt.AccessTokenDto;
import com.example.cargive.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OAuthService {

    private final RestTemplate restTemplate;

    private final UserRepository userRepository;

    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 토큰 발행
     * @param snsType
     * @param postToken
     * @return
     */
    public AccessTokenDto oAuthGetUserInfo(SNSType snsType, PostToken postToken) {
        String email = getSnsEmail(snsType, postToken);
        Optional<User> savedUser = userRepository.findByEmail(email);
        if (savedUser.isEmpty()) {
            User user = User.builder()
                    .email(email)
                    .snsType(snsType)
                    .isDeleted(false)
                    .profilePicture(null)
                    .build();
            userRepository.save(user);
        }

        return jwtTokenProvider.generateAccessTokenDto(email);
    }

    /**
     * SNS 이메일 가져오기
     * @param snsType
     * @param postToken
     * @return
     */
    public String getSnsEmail(SNSType snsType, PostToken postToken) {
        switch (snsType) {
            case KAKAO -> {
                return getKakaoEmail(postToken);
            }
            case GOOGLE -> {
                return getGoogleEmail(postToken);
            }
            case NAVER -> {
                return getNaverEmail(postToken);
            }
        }
        return "";
    }

    /**
     * 카카오 이메일 가져오기
     * @param postToken
     * @return
     */
    public String getKakaoEmail(PostToken postToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + postToken.getAccessToken());
        headers.setContentType(MediaType.valueOf(MediaType.APPLICATION_FORM_URLENCODED_VALUE));

        URI uri = UriComponentsBuilder
                .fromUriString("https://kapi.kakao.com/v2/user/me")
                .encode().build().toUri();
        ResponseEntity<String> res = restTemplate.exchange(uri, HttpMethod.GET, new HttpEntity<>(headers),
                String.class);
        JSONObject jsonObject = (JSONObject) JSONValue.parse(Objects.requireNonNull(res.getBody()));
        JSONObject accountObject = (JSONObject) jsonObject.get("kakao_account");
        return accountObject.get("email").toString();
    }

    /**
     * 구글 이메일 가져오기
     * @param postToken
     * @return
     */
    public String getGoogleEmail(PostToken postToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + postToken.getAccessToken());

        URI uri = UriComponentsBuilder
                .fromUriString("https://www.googleapis.com/oauth2/v1/userinfo")
                .build().toUri();
        ResponseEntity<String> res = restTemplate.exchange(uri, HttpMethod.GET, new HttpEntity<>(headers),
                String.class);
        JSONObject jsonObject = (JSONObject) JSONValue.parse(Objects.requireNonNull(res.getBody()));
        return jsonObject.get("email").toString();
    }

    /**
     * 네이버 이메일 가져오기
     * @param postToken
     * @return
     */
    public String getNaverEmail(PostToken postToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + postToken.getAccessToken());

        URI uri = UriComponentsBuilder
                .fromUriString("https://openapi.naver.com/v1/nid/me")
                .build().toUri();
        ResponseEntity<String> res = restTemplate.exchange(uri, HttpMethod.GET, new HttpEntity<>(headers),
                String.class);
        JSONObject jsonObject = (JSONObject) JSONValue.parse(Objects.requireNonNull(res.getBody()));
        JSONObject response = (JSONObject) jsonObject.get("response");
        return response.get("email").toString();
    }
}
