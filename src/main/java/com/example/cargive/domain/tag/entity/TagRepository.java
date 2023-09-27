package com.example.cargive.domain.tag.entity;

import com.example.cargive.domain.tag.infra.TagQueryRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long>, TagQueryRepository {
}