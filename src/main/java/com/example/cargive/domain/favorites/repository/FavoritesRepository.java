package com.example.cargive.domain.favorites.repository;

import com.example.cargive.domain.favorites.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FavoritesRepository extends JpaRepository<Favorite, Long> {

}
