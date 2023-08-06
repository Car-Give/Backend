package com.example.cargive.security.jwt;

import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.SneakyThrows;
import org.springframework.security.oauth2.jwt.BadJwtException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;

import java.text.ParseException;

public class JwtDecoderImpl implements JwtDecoder {

    private final JwtProvider jwtProvider;

    public JwtDecoderImpl(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @SneakyThrows
    @Override
    public Jwt decode(String token) throws JwtException {

        try {
            SignedJWT jwt = jwtProvider.decode(token);

            JWTClaimsSet claims = jwt.getJWTClaimsSet();

            return Jwt.withTokenValue(token)
                    .subject(claims.getSubject())
                    .jti(claims.getJWTID())
                    .claims(stringObjectMap -> stringObjectMap.putAll(claims.getClaims()))
                    .headers(stringObjectMap -> stringObjectMap.putAll(jwt.getHeader().toJSONObject()))
                    .issuedAt(claims.getIssueTime().toInstant())
                    .build();
        } catch (ParseException e) {
            throw new BadJwtException(e.getMessage());
        }
    }
}
