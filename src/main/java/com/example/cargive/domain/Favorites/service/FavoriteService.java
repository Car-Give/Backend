package com.example.cargive.domain.Favorites.service;


import com.example.cargive.domain.Favorites.dto.mapper.FavoriteMapper;
import com.example.cargive.domain.Favorites.dto.request.FavoriteCreateRequest;
import com.example.cargive.domain.Favorites.entity.Favorite;
import com.example.cargive.domain.Favorites.exception.FavoriteNotFoundException;
import com.example.cargive.domain.Favorites.repository.FavoritesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoritesRepository favoritesRepository;
    private final FavoriteMapper favoriteMapper;

    public void createFavorite(FavoriteCreateRequest request){
       Favorite newFavorite = favoriteMapper.toCreateRequest(request);
       favoritesRepository.save(newFavorite);
    }

    public void deleteFavorite(Long favoriteId){
        Favorite deleteFavorite = findFavoriteById(favoriteId);
        favoritesRepository.delete(deleteFavorite);
    }

    @Transactional(readOnly = true)
    public Favorite findFavoriteById(Long favoriteId){
        return favoritesRepository.findById(favoriteId).orElseThrow(FavoriteNotFoundException::new);
    }
}
