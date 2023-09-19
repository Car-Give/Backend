package com.example.cargive.domain.favorite.infra.query;

import com.example.cargive.domain.favorite.controller.dto.response.FavoritePkGroupResponse;
import com.example.cargive.domain.favorite.controller.dto.response.QFavoritePkGroupResponse;
import com.example.cargive.domain.favorite.infra.query.dto.FavoriteQueryResponse;
import com.example.cargive.global.template.Status;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.cargive.domain.favorite.entity.QFavoritePkGroup.favoritePkGroup;

@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FavoriteGroupQueryRepositoryImpl implements FavoriteGroupQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public FavoriteQueryResponse<FavoritePkGroupResponse> getMyFavoritePkGroupByTime(Long memberId, int page) {
        Pageable pageable = PageRequest.of(page, 6);

        List<FavoritePkGroupResponse> responseList = jpaQueryFactory
                .selectDistinct(new QFavoritePkGroupResponse(favoritePkGroup.id, favoritePkGroup.name, favoritePkGroup.createAt))
                .from(favoritePkGroup)
                .where(favoritePkGroup.status.eq(Status.NORMAL), favoritePkGroup.member.id.eq(memberId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(favoritePkGroup.createAt.desc())
                .fetch();

        JPAQuery<Long> jpaQuery = jpaQueryFactory
                .select(favoritePkGroup.countDistinct())
                .from(favoritePkGroup)
                .where(favoritePkGroup.status.eq(Status.NORMAL))
                .orderBy(favoritePkGroup.createAt.desc());

        return new FavoriteQueryResponse<>(PageableExecutionUtils.getPage(responseList, pageable, jpaQuery::fetchOne));
    }

    @Override
    public FavoriteQueryResponse<FavoritePkGroupResponse> getMyFavoritePkGroupByTitle(Long memberId, int page) {
        Pageable pageable = PageRequest.of(page, 6);

        List<FavoritePkGroupResponse> responseList = jpaQueryFactory
                .selectDistinct(new QFavoritePkGroupResponse(favoritePkGroup.id, favoritePkGroup.name, favoritePkGroup.createAt))
                .from(favoritePkGroup)
                .where(favoritePkGroup.status.eq(Status.NORMAL), favoritePkGroup.member.id.eq(memberId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(favoritePkGroup.name.asc())
                .fetch();

        JPAQuery<Long> jpaQuery = jpaQueryFactory
                .select(favoritePkGroup.countDistinct())
                .from(favoritePkGroup)
                .where(favoritePkGroup.status.eq(Status.NORMAL))
                .orderBy(favoritePkGroup.name.asc());

        return new FavoriteQueryResponse<>(PageableExecutionUtils.getPage(responseList, pageable, jpaQuery::fetchOne));
    }
}