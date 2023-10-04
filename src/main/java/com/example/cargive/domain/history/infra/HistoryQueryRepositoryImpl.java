package com.example.cargive.domain.history.infra;

import com.example.cargive.domain.history.controller.dto.HistoryResponse;
import com.example.cargive.domain.history.controller.dto.QHistoryResponse;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.example.cargive.domain.history.entity.QHistory.*;

@RequiredArgsConstructor
public class HistoryQueryRepositoryImpl implements HistoryQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<HistoryResponse> findHistoryListByCarId(Long carId, Long memberId) {
         return jpaQueryFactory
                .selectDistinct(new QHistoryResponse(history.id, history.createAt, history.updateAt, history.status))
                .from(history)
                .where(history.car.id.eq(carId), history.car.member.id.eq(memberId))
                .orderBy(history.status.desc(), history.createAt.asc())
                .fetch();
    }
}