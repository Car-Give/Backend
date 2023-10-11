package com.example.cargive.answer.service;

import com.example.cargive.common.ServiceTest;
import com.example.cargive.domain.answer.controller.dto.AnswerRequest;
import com.example.cargive.domain.answer.entity.Answer;
import com.example.cargive.domain.answer.service.AnswerService;
import com.example.cargive.domain.member.entity.Member;
import com.example.cargive.domain.question.entity.Question;
import com.example.cargive.global.base.BaseException;
import com.example.cargive.global.template.Status;
import com.example.cargive.member.fixture.MemberFixture;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;

import static com.example.cargive.answer.fixture.AnswerFixture.*;
import static com.example.cargive.question.fixture.QuestionFixture.*;
import static org.assertj.core.api.Assertions.*;

@DisplayName("Answer [Service Layer] -> answerService 테스트")
public class AnswerServiceTest extends ServiceTest {
    @Autowired
    private AnswerService answerService;
    private Member member;
    private Question question;
    private Question otherQuestion;
    private Answer answer;
    private Long answerId;
    private Long questionId;
    private Long otherQuestionId;
    private Long errorAnswerId = Long.MAX_VALUE;
    private Long errorQuestionId = Long.MAX_VALUE;

    @BeforeEach
    public void initTest() {
        member = MemberFixture.WIZ.toMember();
        question = QUESTION_0.toQuestion(member);
        otherQuestion = QUESTION_1.toQuestion(member);
        answer = ANSWER_1.createEntity(question);

        memberRepository.save(member);
        questionId = questionRepository.save(question).getId();
        otherQuestionId = questionRepository.save(otherQuestion).getId();
        answerId = answerRepository.save(answer).getId();
    }

    @AfterEach
    public void afterTest() {
        answerRepository.deleteAll();
    }

    @Nested
    @DisplayName("답변 생성")
    public class createAnswerTest {
        @Test
        @DisplayName("유효하지 않은 questionId로 요청하는 경우, 오류를 반환한다")
        public void throwExceptionByInvalidQuestionId() {
            // given
            AnswerRequest request = getAnswerRequest();

            // when - then
            assertThatThrownBy(() -> answerService.createAnswer(request, errorQuestionId))
                    .isInstanceOf(BaseException.class);
        }

        @Test
        @DisplayName("답변 생성에 성공한다")
        public void successCreateAnswer() {
            // given
            AnswerRequest request = getAnswerRequest();

            // when
            answerService.createAnswer(request, otherQuestionId);

            // then
            assertThat(answerRepository.findAll().size()).isEqualTo(2);
        }
    }

    @Nested
    @DisplayName("답변 수정")
    public class editAnswerTest {
        @Test
        @DisplayName("유효하지 않은 answerId로 요청하는 경우, 오류를 반환한다")
        public void throwExceptionByInvalidAnswerId() {
            // given
            AnswerRequest request = getAnswerRequest();

            // when - then
            assertThatThrownBy(() -> answerService.editAnswer(request, errorAnswerId))
                    .isInstanceOf(BaseException.class);
        }

        @Test
        @DisplayName("답변 수정에 성공한다")
        public void successEditAnswer() {
            // given
            AnswerRequest request = getAnswerRequest();

            // when
            answerService.editAnswer(request, answerId);

            // then
            assertThat(answer.getContent()).isEqualTo(request.content());
        }
    }

    @Nested
    @DisplayName("답변 삭제")
    public class deleteAnswerTest {
        @Test
        @DisplayName("유효하지 않은 answerId로 요청하는 경우, 오류를 반환한다")
        public void throwExceptionByInvalidAnswerId() {
            // when - then
            assertThatThrownBy(() -> answerService.deleteAnswer(errorAnswerId))
                    .isInstanceOf(BaseException.class);
        }

        @Test
        @DisplayName("답변 삭제에 성공한다")
        public void successDeleteAnswer() {
            // when
            answerService.deleteAnswer(answerId);

            // then
            assertThat(answer.getStatus()).isEqualTo(Status.EXPIRED);
        }
    }

    private AnswerRequest getAnswerRequest() {
        return new AnswerRequest("Content");
    }
}
