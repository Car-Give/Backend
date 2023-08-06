package com.example.cargive.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AccessToken {

    @JsonProperty("access_token")
    private String accessToken;
}
