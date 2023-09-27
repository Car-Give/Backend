package com.example.cargive.domain.favorite.entity.sortCondition;

import com.example.cargive.global.base.BaseException;
import com.example.cargive.global.base.BaseResponseStatus;
import com.example.cargive.global.template.EnumStandard;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

// 즐겨찾기 그룹의 정렬 조건들을 관리하기 위한 파일일
@Getter
@AllArgsConstructor
public enum FavoriteGroupSortCondition implements EnumStandard {
    TIME("최신순"),
    TITLE("제목순")
    ;
    private final String value;

    public static FavoriteGroupSortCondition from(String value) {
        return Arrays.stream(values())
                .filter(sortCondition -> sortCondition.value.equals(value))
                .findFirst()
                .orElseThrow(() -> new BaseException(BaseResponseStatus.NOT_FOUND_SORT_CONDITION));
    }
}
