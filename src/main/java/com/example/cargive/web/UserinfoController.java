package com.example.cargive.web;

import com.example.cargive.security.SignedUser;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserinfoController {

    @GetMapping(value = "/me")
    public SignedUser me(@AuthenticationPrincipal SignedUser signedUser) {
        return signedUser;
    }
}
