package com.example.cargive.domain.question.infra.query;

import com.example.cargive.domain.member.entity.Member;
import com.example.cargive.domain.question.service.response.GetListOfQuestionResponse;
import com.example.cargive.domain.question.service.response.QGetListOfQuestionResponse;
import com.example.cargive.global.template.Status;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.cargive.domain.question.entity.QQuestion.question;

@Transactional(readOnly = true)
@RequiredArgsConstructor
public class QuestionFindQueryRepositoryImpl implements QuestionFindQueryRepository {
    private final JPAQueryFactory query;
    private final int PAGE_SIZE = 6;

    @Override
    public List<GetListOfQuestionResponse> findPageOrderByCreateAt(Member member, LocalDateTime createAt) {
        return query.selectDistinct(new QGetListOfQuestionResponse(question.id, question.title, question.createAt))
                .from(question)
                .where(question.status.eq(Status.NORMAL), question.member.eq(member), getOffsetCondition(createAt))
                .orderBy(question.createAt.desc())
                .limit(PAGE_SIZE)
                .fetch();
    }

    private BooleanExpression getOffsetCondition(LocalDateTime createAt) {
        if (createAt == null) return null;

        return question.createAt.before(createAt);
    }
}
