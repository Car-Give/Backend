package com.example.cargive.domain.favorites.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FavoriteCreateRequest {

    @NotBlank(message = "즐겨찾기 이름은 필수 입니다.")
    private String name;

}
