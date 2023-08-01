package com.example.cargive.domain.Favorites.controller;


import com.example.cargive.domain.Favorites.dto.request.FavoriteCreateRequest;
import com.example.cargive.domain.Favorites.service.FavoriteService;
import com.example.cargive.global.result.ResultCode;
import com.example.cargive.global.result.ResultResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/favorites")
public class FavoriteController {

    private final FavoriteService favoriteService;

    @PostMapping
    public ResponseEntity<ResultResponse> createFavorite(@Valid @RequestBody FavoriteCreateRequest createRequest){
        favoriteService.createFavorite(createRequest);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.FAVORITE_CREATE_SUCCESS));
    }

}
