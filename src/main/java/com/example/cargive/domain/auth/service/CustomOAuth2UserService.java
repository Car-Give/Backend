package com.example.cargive.domain.auth.service;

import com.example.cargive.domain.auth.dto.OAuthAttributes;
import com.example.cargive.domain.auth.dto.UserProfile;
import com.example.cargive.domain.member.entity.Member;
import com.example.cargive.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService oAuth2UserService = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        String userNameAttributeName = userRequest
                .getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();

        Map<String, Object> attributes = oAuth2User.getAttributes();

        UserProfile userProfile = OAuthAttributes.extract(registrationId, attributes);

        saveUser(userProfile);

        Map<String, Object> customAttribute =
                getCustomAttribute(registrationId, userNameAttributeName, attributes, userProfile);

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(userProfile.getSocial().getValue())),
                customAttribute,
                userNameAttributeName);
    }

    public Map<String, Object> getCustomAttribute(String registrationId,
                                                  String userNameAttributeName,
                                                  Map<String, Object> attributes,
                                                  UserProfile userProfile) {
        Map<String, Object> customAttribute = new ConcurrentHashMap<>();

        customAttribute.put(userNameAttributeName, attributes.get(userNameAttributeName));
        customAttribute.put("provider", registrationId);
        customAttribute.put("name", userProfile.getLoginId());
        customAttribute.put("email", userProfile.getEmail().getValue());

        return customAttribute;
    }

    public void saveUser(UserProfile userProfile) {
        Member member = userProfile.createMemberEntity();

        if(memberRepository.existsMemberByLoginId(member.getLoginId())) return;

        memberRepository.save(member);
    }
}