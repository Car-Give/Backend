package com.example.cargive.domain.Favorites.repository;

import com.example.cargive.domain.Favorites.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FavoritesRepository extends JpaRepository<Favorite, Long> {

}
