package com.example.cargive.domain.favorite.entity.repository;

import com.example.cargive.domain.favorite.entity.FavoritePkInfo;
import com.example.cargive.domain.favorite.infra.query.FavoriteInfoQueryRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoritePkInfoRepository extends JpaRepository<FavoritePkInfo, Long>, FavoriteInfoQueryRepository {
}
