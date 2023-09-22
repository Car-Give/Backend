package com.example.cargive.question.infra.query;

import com.example.cargive.common.RepositoryTest;
import com.example.cargive.domain.member.entity.Member;
import com.example.cargive.domain.member.repository.MemberRepository;
import com.example.cargive.domain.question.entity.repository.QuestionRepository;
import com.example.cargive.domain.question.service.response.GetListOfQuestionResponse;
import com.example.cargive.question.fixture.QuestionFixture;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.example.cargive.member.fixture.MemberFixture.WIZ;
import static com.example.cargive.question.fixture.QuestionFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Question [Repository Layer] -> QuestionFindQueryRepository 테스트")
public class QuestionFindQueryRepositoryImplTest extends RepositoryTest {
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private MemberRepository memberRepository;

    private static Long MEMBER_ID;

    @BeforeEach
    void setup() {
        Member member = memberRepository.save(WIZ.toMember());
        MEMBER_ID = member.getId();
        for (QuestionFixture questionFixture: QuestionFixture.values()) {
            questionRepository.save(questionFixture.toQuestion(member));
        }
    }

    @AfterEach
    void clear() {
        questionRepository.deleteAll();
    }

    @Nested
    @DisplayName("질문 리스트 조회")
    class QuestionList {
        @Test
        @DisplayName("사용자의 질문을 최신순으로 6개씩 반환한다")
        void findQuestionPageOfMemberOrderByCreateAt() {
            Member member = memberRepository.findById(MEMBER_ID).orElseThrow();
            List<GetListOfQuestionResponse> firstPage = questionRepository.findPageOrderByCreateAt(member, null);

            assertAll(
                    () -> assertThat(firstPage.get(0).id()).isEqualTo(QUESTION_6.getId()),
                    () -> assertThat(firstPage.get(1).id()).isEqualTo(QUESTION_5.getId()),
                    () -> assertThat(firstPage.get(2).id()).isEqualTo(QUESTION_4.getId()),
                    () -> assertThat(firstPage.get(3).id()).isEqualTo(QUESTION_3.getId()),
                    () -> assertThat(firstPage.get(4).id()).isEqualTo(QUESTION_2.getId()),
                    () -> assertThat(firstPage.get(5).id()).isEqualTo(QUESTION_1.getId()),
                    () -> assertThat(firstPage.size()).isEqualTo(6)
            );
        }
    }
}
