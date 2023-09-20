package com.example.cargive.domain.favorite.service;

import com.example.cargive.domain.favorite.entity.Favorite;
import com.example.cargive.domain.favorite.entity.FavoriteCar;
import com.example.cargive.domain.favorite.entity.FavoritePkGroup;
import com.example.cargive.domain.favorite.entity.repository.FavoriteRepository;
import com.example.cargive.domain.member.entity.Member;
import com.example.cargive.global.base.BaseResponseStatus;
import com.example.cargive.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// Repository를 외부에 직접적으로 노출하는 것을 피하기 위하여 별도의 Entity 조회 서비스 생성
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FavoritePkGroupFindService {
    private final FavoriteRepository favoriteRepository;

    public FavoritePkGroup findFavoriteById(Member member, Long favoriteGroupId) {
        Favorite findValue = favoriteRepository.findById(favoriteGroupId)
                .orElseThrow(() -> new BusinessException(BaseResponseStatus.FAVORITE_NOT_FOUND_ERROR));

        if(findValue instanceof FavoriteCar) throw new BusinessException(BaseResponseStatus.INPUT_INVALID_VALUE);
        checkMemberValidation(member, (FavoritePkGroup) findValue);

        return (FavoritePkGroup)findValue;
    }

    private void checkMemberValidation(Member member, FavoritePkGroup favoritePkGroup) {
        if(!favoritePkGroup.getMember().getName().equals(member.getName()))
            throw new BusinessException(BaseResponseStatus.INPUT_INVALID_VALUE);
    }
}
