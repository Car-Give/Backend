package com.example.cargive.security.jwt;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

public class JwtProvider {

    private final String secretKey;

    private JwtProvider(String secretKey) {
        this.secretKey = secretKey;
    }

    public static JwtProvider create(String secretKey) {
        return new JwtProvider(secretKey);
    }

    public String encode(JwtClaims claims) {

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(claims.getSub())
                .claim("uid", claims.getSub())
                .jwtID(UUID.randomUUID().toString())
                .notBeforeTime(new Date())
                .issueTime(new Date())
                .build();

        return encode((HashMap<String, Object>) claimsSet.toJSONObject());
    }

    public String encode(JWTClaimsSet claimsSet) {
        JWSObject jwsObject = new JWSObject(new JWSHeader(JWSAlgorithm.HS256), new Payload(claimsSet.toJSONObject()));
        try {
            jwsObject.sign(new MACSigner(this.secretKey));
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
        return jwsObject.serialize();
    }

    public String encode(HashMap<String, Object> claimsMap) {
        JWTClaimsSet.Builder builder = new JWTClaimsSet.Builder();

        for (String key : claimsMap.keySet()) {
            builder.claim(key, claimsMap.get(key));
        }

        builder.jwtID(UUID.randomUUID().toString());
        builder.issueTime(new Date());

        return encode(builder.build());
    }

    public SignedJWT parse(String token) throws ParseException {
        return SignedJWT.parse(token);
    }

    public SignedJWT decode(String token) throws ParseException {
        SignedJWT parse = this.parse(token);
        return parse;
    }

    public boolean verify(String token) throws JOSEException, ParseException {
        SignedJWT jwt = this.parse(token);
        return jwt.verify(new MACVerifier(this.secretKey));
    }
}
