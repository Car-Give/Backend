package com.example.cargive.domain.Favorites.dto.mapper;

import com.example.cargive.domain.Favorites.dto.request.FavoriteCreateRequest;
import com.example.cargive.domain.Favorites.entity.Favorite;
import org.springframework.stereotype.Component;

@Component
public class FavoriteMapper {

    public Favorite toCreateRequest(FavoriteCreateRequest request){
        return Favorite.builder()
                .name(request.getName())
                .count(0L)
                .build();
    }
}
