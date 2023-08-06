package com.example.cargive.idp;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class IdpUserinfoImpl implements IdpUserinfo {

    private String id;

    private String email;

    private String username;

    private String profile;

    private LocalDate birthday;
}
