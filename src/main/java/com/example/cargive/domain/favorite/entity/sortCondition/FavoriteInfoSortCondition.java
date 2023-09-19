package com.example.cargive.domain.favorite.entity.sortCondition;

import com.example.cargive.domain.favorite.exception.FavoriteErrorStatus;
import com.example.cargive.global.base.BaseException;
import com.example.cargive.global.template.EnumStandard;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

// 즐겨찾기된 주차장의 정렬 조건들을 관리하기 위한 파일
@Getter
@AllArgsConstructor
public enum FavoriteInfoSortCondition implements EnumStandard {
    TIME("최신순")
    ;

    private final String value;

    public static FavoriteInfoSortCondition from(String value) {
        try {
            return Arrays.stream(values())
                    .filter(sortCondition -> sortCondition.value.equals(value))
                    .findFirst()
                    .orElseThrow(() -> new BaseException(FavoriteErrorStatus.NOT_FOUND_SORT_CONDITION));
        } catch(BaseException e) {
            throw new RuntimeException();
        }
    }
}
