package com.example.cargive.global.config.oauth2;

import com.example.cargive.domain.member.repository.MemberRepository;
import com.example.cargive.global.base.BaseException;
import com.example.cargive.global.base.BaseResponseStatus;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Value("${oauth.redirect-uri}")
    private String redirectUrl;

    private final MemberRepository memberRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        String redirectUrl = getRedirectUrl(authentication);
        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }

    private Long getMemberId(Authentication authentication) {
        OAuth2User principal = (OAuth2User) authentication.getPrincipal();

        String loginId = principal.getAttribute("name");

        return memberRepository.findMemberByLoginId(loginId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.MEMBER_NOT_FOUND_ERROR)).getId();
    }

    private String getRedirectUrl(Authentication authentication) {
        return UriComponentsBuilder.fromUriString(redirectUrl)
                .queryParam("memberId", getMemberId(authentication))
                .build().toUriString();
    }
}
