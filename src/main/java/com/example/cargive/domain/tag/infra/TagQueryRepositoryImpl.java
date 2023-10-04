package com.example.cargive.domain.tag.infra;

import com.example.cargive.domain.tag.controller.dto.response.QTagResponse;
import com.example.cargive.domain.tag.controller.dto.response.TagResponse;
import com.example.cargive.global.template.Status;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.cargive.domain.tag.entity.QTag.*;

@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TagQueryRepositoryImpl implements TagQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<TagResponse> findTagsByCar(Long carId) {
        return jpaQueryFactory
                .selectDistinct(new QTagResponse(tag.id, tag.name))
                .from(tag)
                .where(tag.car.id.eq(carId), tag.status.eq(Status.NORMAL))
                .orderBy(tag.id.asc())
                .fetch();
    }
}