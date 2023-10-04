package com.example.cargive.domain.history.entity;

import com.example.cargive.domain.history.infra.HistoryQueryRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoryRepository extends JpaRepository<History, Long>, HistoryQueryRepository {
}