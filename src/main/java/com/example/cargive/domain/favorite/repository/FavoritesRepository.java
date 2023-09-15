package com.example.cargive.domain.favorite.repository;

import com.example.cargive.domain.favorite.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FavoritesRepository extends JpaRepository<Favorite, Long> {

}
