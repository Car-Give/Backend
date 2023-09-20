package com.example.cargive.domain.favorite.infra.query;

import com.example.cargive.domain.favorite.controller.dto.response.FavoritePkInfoResponse;
import com.example.cargive.domain.favorite.controller.dto.response.QFavoritePkInfoResponse;
import com.example.cargive.domain.favorite.entity.sortCondition.FavoriteInfoSortCondition;
import com.example.cargive.domain.favorite.infra.query.dto.FavoriteQueryResponse;
import com.example.cargive.global.base.BaseResponseStatus;
import com.example.cargive.global.exception.BusinessException;
import com.example.cargive.global.template.Status;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.cargive.domain.favorite.entity.QFavoritePkInfo.favoritePkInfo;

@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FavoriteInfoQueryRepositoryImpl implements FavoriteInfoQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public FavoriteQueryResponse<FavoritePkInfoResponse> getMyFavoritePkInfo(FavoriteInfoSortCondition sortCondition,
                                                                             Long memberId, Long favoriteGroupId,
                                                                             Long cursorId) {
        List<FavoritePkInfoResponse> pkInfoList = jpaQueryFactory
                .selectDistinct(new QFavoritePkInfoResponse(
                        favoritePkInfo.id,
                        favoritePkInfo.parkingLot,
                        favoritePkInfo.createAt)
                )
                .from(favoritePkInfo)
                .where(getOffsetCondition(sortCondition, cursorId),
                        favoritePkInfo.favoritePkGroup.id.eq(favoriteGroupId),
                        favoritePkInfo.favoritePkGroup.status.eq(Status.NORMAL),
                        favoritePkInfo.parkingLot.status.eq(Status.NORMAL))
                .orderBy(getSortCondition(sortCondition))
                .limit(6)
                .fetch();

        return new FavoriteQueryResponse<>(pkInfoList);
    }

    private BooleanExpression getOffsetCondition(FavoriteInfoSortCondition sortCondition, Long cursorId) {
        if(cursorId == null) return null;

        if(sortCondition.equals(FavoriteInfoSortCondition.TIME)) return favoritePkInfo.id.lt(cursorId);
        throw new BusinessException(BaseResponseStatus.INVALID_ENUM);
    }

    public OrderSpecifier getSortCondition(FavoriteInfoSortCondition sortCondition) {
        if(sortCondition.equals(FavoriteInfoSortCondition.TIME)) return favoritePkInfo.createAt.desc();
        throw new BusinessException(BaseResponseStatus.INVALID_ENUM);
    }
}