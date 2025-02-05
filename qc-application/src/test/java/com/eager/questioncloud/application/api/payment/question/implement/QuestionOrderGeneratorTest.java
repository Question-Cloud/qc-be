package com.eager.questioncloud.application.api.payment.question.implement;

import com.eager.questioncloud.application.business.payment.question.implement.QuestionOrderGenerator;
import com.eager.questioncloud.application.utils.Fixture;
import com.eager.questioncloud.core.domain.payment.model.QuestionOrder;
import com.eager.questioncloud.core.domain.question.enums.QuestionStatus;
import com.eager.questioncloud.core.domain.question.infrastructure.repository.QuestionRepository;
import com.eager.questioncloud.core.domain.question.model.Question;
import com.eager.questioncloud.core.domain.user.infrastructure.repository.UserRepository;
import com.eager.questioncloud.core.domain.user.model.User;
import com.eager.questioncloud.core.domain.userquestion.infrastructure.repository.UserQuestionRepository;
import com.eager.questioncloud.core.domain.userquestion.model.UserQuestion;
import com.eager.questioncloud.core.exception.CoreException;
import com.eager.questioncloud.core.exception.Error;
import java.util.ArrayList;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class QuestionOrderGeneratorTest {
    @Autowired
    private UserQuestionRepository userQuestionRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestionOrderGenerator questionOrderGenerator;

    @AfterEach
    void tearDown() {
        questionRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
        userQuestionRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("Question 주문을 생성할 수 있다.")
    void generateQuestionOrder() {
        // given
        User user = userRepository.save(
            Fixture.fixtureMonkey.giveMeBuilder(User.class)
                .set("uid", null)
                .sample()
        );

        List<Long> questionIds = Fixture.fixtureMonkey.giveMeBuilder(Question.class)
            .set("id", null)
            .set("questionStatus", QuestionStatus.Available)
            .sampleList(10)
            .stream()
            .map(question -> questionRepository.save(question).getId())
            .toList();

        // when
        QuestionOrder questionOrder = questionOrderGenerator.generateQuestionOrder(user.getUid(), questionIds);

        // then
        Assertions.assertThat(questionOrder.getQuestionIds()).containsExactlyInAnyOrderElementsOf(questionIds);
    }

    @Test
    @DisplayName("비활성화 된 Question을 포함한 주문을 생성할 수 없다.")
    void cannotCreateOrderWithUnAvailableQuestion() {
        // given
        User user = userRepository.save(
            Fixture.fixtureMonkey.giveMeBuilder(User.class)
                .set("uid", null)
                .sample()
        );

        List<Long> availableQuestionIds = Fixture.fixtureMonkey.giveMeBuilder(Question.class)
            .set("id", null)
            .set("questionStatus", QuestionStatus.Available)
            .sampleList(10)
            .stream()
            .map(question -> questionRepository.save(question).getId())
            .toList();

        List<Long> unavailableQuestionIds = Fixture.fixtureMonkey.giveMeBuilder(Question.class)
            .set("id", null)
            .set("questionStatus", QuestionStatus.UnAvailable)
            .sampleList(10)
            .stream()
            .map(question -> questionRepository.save(question).getId())
            .toList();

        List<Long> questionIds = new ArrayList<>();
        questionIds.addAll(availableQuestionIds);
        questionIds.addAll(unavailableQuestionIds);

        //when then
        Assertions.assertThatThrownBy(() -> questionOrderGenerator.generateQuestionOrder(user.getUid(), questionIds))
            .isInstanceOf(CoreException.class)
            .hasFieldOrPropertyWithValue("error", Error.UNAVAILABLE_QUESTION);
    }

    @Test
    @DisplayName("이미 구매한 Question은 주문에 포함할 수 없다.")
    void cannotCreateOrderWithAlreadyOwnedQuestion() {
        // given
        User user = userRepository.save(
            Fixture.fixtureMonkey.giveMeBuilder(User.class)
                .set("uid", null)
                .sample()
        );

        Question alreadyOwnedQuestion = questionRepository.save(
            Fixture.fixtureMonkey.giveMeBuilder(Question.class)
                .set("id", null)
                .set("questionStatus", QuestionStatus.Available)
                .sample()
        );

        Question normalQuestion = questionRepository.save(
            Fixture.fixtureMonkey.giveMeBuilder(Question.class)
                .set("id", null)
                .set("questionStatus", QuestionStatus.Available)
                .sample()
        );

        userQuestionRepository.saveAll(UserQuestion.create(user.getUid(), List.of(alreadyOwnedQuestion.getId())));

        // when then
        Assertions.assertThatThrownBy(
                () -> questionOrderGenerator.generateQuestionOrder(user.getUid(), List.of(normalQuestion.getId(), alreadyOwnedQuestion.getId())))
            .isInstanceOf(CoreException.class)
            .hasFieldOrPropertyWithValue("error", Error.ALREADY_OWN_QUESTION);
    }
}