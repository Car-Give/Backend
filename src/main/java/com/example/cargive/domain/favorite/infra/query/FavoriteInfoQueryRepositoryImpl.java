package com.example.cargive.domain.favorite.infra.query;

import com.example.cargive.domain.favorite.controller.dto.response.FavoritePkInfoResponse;
import com.example.cargive.domain.favorite.controller.dto.response.QFavoritePkInfoResponse;
import com.example.cargive.domain.favorite.entity.sortCondition.FavoriteInfoSortCondition;
import com.example.cargive.domain.favorite.infra.query.dto.FavoriteQueryResponse;
import com.example.cargive.global.base.BaseResponseStatus;
import com.example.cargive.global.exception.BusinessException;
import com.example.cargive.global.template.Status;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
                .orderBy(getSortCondition(sortCondition))
                .limit(pageable.getPageSize() + 1)
                .fetch();

        return new FavoriteQueryResponse<>(pkInfoList, pageable);
    }

    public OrderSpecifier getSortCondition(FavoriteInfoSortCondition sortCondition) {
        if(sortCondition.equals(FavoriteInfoSortCondition.TIME)) return favoritePkInfo.createAt.desc();
        throw new BusinessException(BaseResponseStatus.INVALID_ENUM);
    }
}