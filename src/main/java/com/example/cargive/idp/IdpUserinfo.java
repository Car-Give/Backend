package com.example.cargive.idp;

import java.time.LocalDate;

public interface IdpUserinfo {

    String getId();
    String getEmail();
    String getProfile();
    String getUsername();
    LocalDate getBirthday();
}
