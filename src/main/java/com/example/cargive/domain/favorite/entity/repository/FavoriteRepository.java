package com.example.cargive.domain.favorite.entity.repository;

import com.example.cargive.domain.favorite.entity.Favorite;
import com.example.cargive.domain.favorite.infra.query.FavoriteGroupQueryRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long>, FavoriteGroupQueryRepository {
}
