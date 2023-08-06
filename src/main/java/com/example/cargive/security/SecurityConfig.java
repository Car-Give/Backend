package com.example.cargive.security;

import com.example.cargive.security.jwt.JwtDecoderImpl;
import com.example.cargive.security.jwt.JwtProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {


        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authz ->
                        authz
                                .requestMatchers(antMatcher("/api/**")).permitAll()
                                .requestMatchers(antMatcher("/me")).authenticated()
                                .anyRequest().permitAll()
                )
                .oauth2ResourceServer(server -> server.jwt(Customizer.withDefaults()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(new SignedUserAuthenticationProvider(jwtDecoder()));

        return http.build();
    }

    /**
     * spring security 관리에서 제외 할 url 패턴
     *
     * @return
     */
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web
                .ignoring()
                .requestMatchers(antMatcher("/actuator/**"));
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return new JwtDecoderImpl(jwtProvider());
    }

    @Bean
    public JwtProvider jwtProvider() {
        return JwtProvider.create("sadfknasdofnaoibnarewont1nfaskofn");
    }
}
