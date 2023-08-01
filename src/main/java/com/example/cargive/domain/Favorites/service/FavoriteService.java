package com.example.cargive.domain.Favorites.service;


import com.example.cargive.domain.Favorites.dto.mapper.FavoriteMapper;
import com.example.cargive.domain.Favorites.dto.request.FavoriteCreateRequest;
import com.example.cargive.domain.Favorites.entity.Favorite;
import com.example.cargive.domain.Favorites.repository.FavoritesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoritesRepository favoritesRepository;
    private final FavoriteMapper favoriteMapper;

    public void createFavorite(FavoriteCreateRequest request){
       Favorite newFavorite = favoriteMapper.toCreateRequest(request);
       favoritesRepository.save(newFavorite);
    }
}
