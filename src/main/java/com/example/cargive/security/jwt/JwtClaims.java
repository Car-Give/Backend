package com.example.cargive.security.jwt;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.util.HashMap;

@Builder
@Getter
public class JwtClaims implements Serializable {

    private String sub;
    private String role;

    public HashMap<String, Object> toClaimMap() {
        HashMap<String, Object> hm = new HashMap<>();
        hm.put("uid", sub);
        hm.put("role", getRole());
        return hm;
    }
}
