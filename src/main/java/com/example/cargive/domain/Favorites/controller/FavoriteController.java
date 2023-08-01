package com.example.cargive.domain.Favorites.controller;


import com.example.cargive.domain.Favorites.dto.request.FavoriteCreateRequest;
import com.example.cargive.domain.Favorites.service.FavoriteService;
import com.example.cargive.global.result.ResultCode;
import com.example.cargive.global.result.ResultResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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

    @DeleteMapping("/{favoriteId}")
    public ResponseEntity<ResultResponse> deleteFavorite(@PathVariable Long favoriteId){
        favoriteService.deleteFavorite(favoriteId);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.FAVORITE_DELETE_SUCCESS));
    }

}
