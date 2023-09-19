package com.example.cargive.domain.favorite.infra.query.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

// querydsl을 통한 데이터 결과값 반환에 사용되는 DTO 파일
@Getter
@NoArgsConstructor
public class FavoriteQueryResponse<T> {
    private PageInfo pageInfo;
    private List<T> favoriteList = new ArrayList<>();

    // 페이지가 1부터 시작할 수 있도록 1을 더해줌
    public FavoriteQueryResponse(Page<T> page) {
        this.favoriteList = page.getContent();
        this.pageInfo = new PageInfo(page.getNumber() + 1, page.getSize(), page.isFirst(), page.isLast());
    }

    @Getter
    @NoArgsConstructor
    private static class PageInfo {
        private int currentPage; // 현재 페이지
        private int size; // 페이지 내부 데이터의 수
        private boolean isFirst; // 데이터 목록의 처음인가를 표기
        private boolean isLast; // 데이터 목록의 마지막인가를 표기

        public PageInfo (int currentPage, int size, boolean isFirst, boolean isLast) {
            this.currentPage = currentPage;
            this.size = size;
            this.isFirst = isFirst;
            this.isLast = isLast;
        }
    }
}