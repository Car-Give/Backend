package com.example.cargive.idp;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Builder
@Getter
public class KakaoUserinfo implements IdpUserinfo{
    private String id;

    private String email;

    private String username;

    private String profile;

    private LocalDate birthday;
}
