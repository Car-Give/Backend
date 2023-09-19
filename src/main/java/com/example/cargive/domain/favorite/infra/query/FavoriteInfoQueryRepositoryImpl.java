package com.example.cargive.domain.favorite.infra.query;

import com.example.cargive.domain.favorite.controller.dto.response.FavoritePkInfoResponse;
import com.example.cargive.domain.favorite.controller.dto.response.QFavoritePkInfoResponse;
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
import static com.example.cargive.domain.favorite.entity.QFavoritePkInfo.favoritePkInfo;

@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FavoriteInfoQueryRepositoryImpl implements FavoriteInfoQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public FavoriteQueryResponse<FavoritePkInfoResponse> getMyFavoritePkInfo(Long memberId,
                                                                             Long favoriteGroupId,
                                                                             int page) {
        Pageable pageable = PageRequest.of(page, 6);

        List<FavoritePkInfoResponse> pkInfoList = jpaQueryFactory
                .selectDistinct(new QFavoritePkInfoResponse(
                        favoritePkInfo.id,
                        favoritePkInfo.parkingLot,
                        favoritePkInfo.createAt)
                )
                .from(favoritePkInfo)
                .where(favoritePkInfo.favoritePkGroup.id.eq(favoriteGroupId),
                        favoritePkInfo.favoritePkGroup.status.eq(Status.NORMAL),
                        favoritePkInfo.parkingLot.status.eq(Status.NORMAL))
                .orderBy(favoritePkInfo.createAt.desc())
                .fetch();

        JPAQuery<Long> jpaQuery = jpaQueryFactory
                .select(favoritePkGroup.countDistinct())
                .from(favoritePkGroup)
                .where(favoritePkGroup.status.eq(Status.NORMAL))
                .orderBy(favoritePkGroup.createAt.desc());

        return new FavoriteQueryResponse<>(PageableExecutionUtils.getPage(pkInfoList, pageable, jpaQuery::fetchOne));
    }
}
