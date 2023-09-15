package com.example.cargive.domain.favorite.controller;


import com.example.cargive.domain.favorite.dto.request.FavoriteCreateRequest;
import com.example.cargive.domain.favorite.service.FavoriteService;
import com.example.cargive.global.result.ResultCode;
import com.example.cargive.global.result.ResultResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
        return ResponseEntity.status(HttpStatus.CREATED).body(ResultResponse.of(ResultCode.FAVORITE_CREATE_SUCCESS));
    }

    @DeleteMapping("/{favoriteId}")
    public ResponseEntity<ResultResponse> deleteFavorite(@PathVariable Long favoriteId){
        favoriteService.deleteFavorite(favoriteId);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.FAVORITE_DELETE_SUCCESS));
    }

}
