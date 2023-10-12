package com.example.cargive.domain.notice.infra;

import com.example.cargive.domain.notice.infra.dto.NoticeQueryResponse;
import com.example.cargive.domain.notice.infra.dto.QNoticeQueryResponse;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.cargive.domain.notice.entity.QNotice.*;

@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NoticeQueryRepositoryImpl implements NoticeQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;
    private final int PAGE_SIZE = 6;

    @Override
    public List<NoticeQueryResponse> getNoticeResponseList() {
        return jpaQueryFactory
                .selectDistinct(new QNoticeQueryResponse(notice.title, notice.createAt))
                .from(notice)
                .orderBy(notice.createAt.desc())
                .limit(PAGE_SIZE)
                .fetch();
    }
}
