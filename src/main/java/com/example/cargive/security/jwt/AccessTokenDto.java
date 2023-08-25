package com.example.cargive.security.jwt;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class AccessTokenDto {

    private String grantType;

    private String accessToken;

    private Long tokenExpiresIn;
}
