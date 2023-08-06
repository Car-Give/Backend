package com.example.cargive.security;

import org.springframework.security.oauth2.jwt.Jwt;

import java.security.Principal;
import java.util.Map;

public class SignedUser implements Principal {

    private Jwt jwt;
    private final String id;

    public SignedUser(Jwt jwt) {
        this.id = jwt.getSubject();
        this.jwt = jwt;
    }

    @Override
    public String getName() {
        return id;
    }

    public String getId() {
        return id;
    }

    public Map<String, Object> getClaims() {
        return jwt.getClaims();
    }
}