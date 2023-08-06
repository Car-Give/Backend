    package com.example.cargive.idp.client;

    import com.example.cargive.idp.IdpUserinfo;
    import com.example.cargive.idp.IdpUserinfoImpl;
    import com.fasterxml.jackson.core.JsonProcessingException;
    import com.fasterxml.jackson.core.type.TypeReference;
    import com.fasterxml.jackson.databind.ObjectMapper;
    import org.springframework.http.HttpEntity;
    import org.springframework.http.HttpHeaders;
    import org.springframework.http.ResponseEntity;
    import org.springframework.util.ObjectUtils;
    import org.springframework.web.client.RestTemplate;

    import java.time.LocalDate;
    import java.time.format.DateTimeFormatter;
    import java.util.Collections;
    import java.util.HashMap;
    import java.util.Objects;

    public class KakaoClient implements IdpClient {

        public static final KakaoClient instance = new KakaoClient();

        @SuppressWarnings("unchecked")
        @Override
        public IdpUserinfo userinfo(String accessToken) {

            RestTemplate template = new RestTemplate();

            HttpHeaders headers = new HttpHeaders() {{
                put("Authorization", Collections.singletonList(String.format("Bearer %s", accessToken)));
            }};

            HttpEntity<HttpHeaders> entity = new HttpEntity<>(null, headers);

            ResponseEntity<String> response = template.postForEntity("https://kapi.kakao.com/v2/user/me", entity, String.class);

            ObjectMapper mapper = new ObjectMapper();
            TypeReference<HashMap<String, Object>> typeRef = new TypeReference<>() {
            };
            HashMap<String, Object> result;
            try {
                result = mapper.readValue(response.getBody(), typeRef);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

            String id = Objects.requireNonNull(result).get("id").toString();

            HashMap<String, Object> kakaoAccount = (HashMap<String, Object>) result.get("kakao_account");

            String name = null;
            String email = null;

            LocalDate birthday = null;
            String profile = null;

            if (kakaoAccount != null && !kakaoAccount.isEmpty()) {
                name = (String) kakaoAccount.get("name");
                email = (String) kakaoAccount.get("email");

                if (
                        !ObjectUtils.isEmpty(kakaoAccount.get("birthyear"))
                                && !ObjectUtils.isEmpty(kakaoAccount.get("birthday"))
                ) {
                    // 20220303
                    String birthdayString = kakaoAccount.get("birthyear").toString() + kakaoAccount.get("birthday");
                    birthday = LocalDate.parse(birthdayString, DateTimeFormatter.ofPattern("yyyyMMdd"));
                }

                HashMap<String, Object> kakaoProfile = (HashMap<String, Object>) kakaoAccount.get("profile");
                if (!kakaoProfile.isEmpty()) {
                    if (ObjectUtils.isEmpty(name)) {
                        name = (String) kakaoProfile.get("nickname");
                    }

                    profile = (String) kakaoProfile.get("profile_image_url");
                }
            }

            return IdpUserinfoImpl.builder()
                    .id(id)
                    .birthday(birthday)
                    .email(email)
                    .username(name)
                    .profile(profile)
                    .build();
        }
    }