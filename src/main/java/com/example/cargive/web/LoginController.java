package com.example.cargive.web;

import com.example.cargive.idp.IdpUserinfo;
import com.example.cargive.idp.client.KakaoClient;
import com.example.cargive.security.jwt.JwtClaims;
import com.example.cargive.security.jwt.JwtProvider;
import com.example.cargive.web.dto.AccessToken;
import com.example.cargive.web.dto.PostToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    @Autowired
    private JwtProvider jwtProvider;

    @PostMapping(value = "/auth/login/{providerId}")
    public AccessToken postToken(
            @RequestBody PostToken postToken,
            @PathVariable String providerId
    ) {
        IdpUserinfo userinfo = null;
        if ("kakao".equals(providerId)) {
            userinfo = KakaoClient.instance.userinfo(postToken.getAccessToken());
        }

        if (userinfo == null) {
            throw new IllegalArgumentException("지원하지 않는 provider");
        }

        return postToken(userinfo);
    }

    private AccessToken postToken(IdpUserinfo userinfo) {
        JwtClaims claims = JwtClaims.builder()
                .sub(userinfo.getId())
                .build();

        String accessToken = jwtProvider.encode(claims);

        return AccessToken.builder().accessToken(accessToken).build();
    }
}
