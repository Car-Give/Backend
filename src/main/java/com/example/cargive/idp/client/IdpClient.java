package com.example.cargive.idp.client;

import com.example.cargive.idp.IdpUserinfo;

public interface IdpClient {
    IdpUserinfo userinfo(String accessToken);
}
