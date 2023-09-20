package com.example.cargive.domain.favorite.infra.query;

import com.example.cargive.domain.favorite.controller.dto.response.FavoritePkGroupResponse;
import com.example.cargive.domain.favorite.controller.dto.response.QFavoritePkGroupResponse;
import com.example.cargive.domain.favorite.entity.sortCondition.FavoriteGroupSortCondition;
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

import static com.example.cargive.domain.favorite.entity.QFavoritePkGroup.favoritePkGroup;

@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FavoriteGroupQueryRepositoryImpl implements FavoriteGroupQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public FavoriteQueryResponse<FavoritePkGroupResponse> getMyFavoritePkGroup(FavoriteGroupSortCondition sortCondition,
                                                                                     Long memberId, int page) {
        Pageable pageable = PageRequest.of(page, 6);

        List<FavoritePkGroupResponse> responseList = jpaQueryFactory
                .selectDistinct(new QFavoritePkGroupResponse(favoritePkGroup.id, favoritePkGroup.name, favoritePkGroup.createAt))
                .from(favoritePkGroup)
                .where(favoritePkGroup.status.eq(Status.NORMAL), favoritePkGroup.member.id.eq(memberId))
                .orderBy(getSortCondition(sortCondition))
                .limit(pageable.getPageSize() + 1)
                .fetch();

        return new FavoriteQueryResponse<>(responseList, pageable);
    }

    private OrderSpecifier getSortCondition(FavoriteGroupSortCondition sortCondition) {
        if(sortCondition.equals(FavoriteGroupSortCondition.TIME)) return favoritePkGroup.createAt.desc();
        if(sortCondition.equals(FavoriteGroupSortCondition.TITLE)) return favoritePkGroup.name.asc();
        throw new BusinessException(BaseResponseStatus.INVALID_ENUM);
    }
}