package com.example.cargive.domain.car.infra;

import com.example.cargive.domain.car.controller.dto.response.CarResponse;
import com.example.cargive.domain.tag.controller.dto.response.TagResponse;
import com.example.cargive.global.template.Status;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.cargive.domain.car.entity.QCar.*;
import static com.example.cargive.domain.tag.entity.QTag.*;
import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;

@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CarQueryRepositoryImpl implements CarQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<CarResponse> findCarByMemberId(Long memberId) {
        return jpaQueryFactory
                .selectFrom(car)
                .leftJoin(car.tagList, tag)
                .orderBy(car.isFavorite.desc(), car.createAt.desc())
                .where(car.status.eq(Status.NORMAL), car.member.id.eq(memberId))
                .transform(groupBy(car.id).list(Projections.constructor(CarResponse.class,
                        car.id, car.type, car.number, car.recentCheck, car.mileage,
                        car.imageUrl, car.isFavorite,
                        list(Projections.constructor(TagResponse.class, tag.id, tag.name))
                )));
    }
}