package com.example.cargive.domain.notice.entity.repository;

import com.example.cargive.domain.notice.entity.Notice;
import com.example.cargive.domain.notice.infra.NoticeQueryRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long>, NoticeQueryRepository {
}
