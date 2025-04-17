package com.eager.questioncloud.application.api.payment.question.implement

import com.eager.questioncloud.application.utils.Fixture
import com.eager.questioncloud.core.domain.question.enums.QuestionStatus
import com.eager.questioncloud.core.domain.question.infrastructure.repository.QuestionRepository
import com.eager.questioncloud.core.domain.question.model.Question
import com.eager.questioncloud.core.domain.user.infrastructure.repository.UserRepository
import com.eager.questioncloud.core.domain.user.model.User
import com.eager.questioncloud.core.domain.userquestion.infrastructure.repository.UserQuestionRepository
import com.eager.questioncloud.core.domain.userquestion.model.UserQuestion.Companion.create
import com.eager.questioncloud.core.exception.CoreException
import com.eager.questioncloud.core.exception.Error
import com.navercorp.fixturemonkey.kotlin.giveMeKotlinBuilder
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
internal class QuestionOrderGeneratorTest(
    @Autowired val userQuestionRepository: UserQuestionRepository,
    @Autowired val questionRepository: QuestionRepository,
    @Autowired val userRepository: UserRepository,
    @Autowired val questionOrderGenerator: QuestionOrderGenerator,
) {
    @AfterEach
    fun tearDown() {
        questionRepository.deleteAllInBatch()
        userRepository.deleteAllInBatch()
        userQuestionRepository.deleteAllInBatch()
    }

    @Test
    fun `Question 주문을 생성할 수 있다`() {
        // given
        val user = userRepository.save(
            Fixture.fixtureMonkey.giveMeKotlinBuilder<User>()
                .set(User::uid, null)
                .build()
                .sample()
        )

        val questionIds = Fixture.fixtureMonkey.giveMeKotlinBuilder<Question>()
            .set(Question::id, null)
            .set(Question::questionStatus, QuestionStatus.Available)
            .sampleList(10)
            .stream()
            .map { question ->
                questionRepository.save(question).id
            }
            .toList()

        // when
        val questionOrder = questionOrderGenerator.generateQuestionOrder(user.uid, questionIds)

        // then
        Assertions.assertThat(questionOrder.questionIds).containsExactlyInAnyOrderElementsOf(questionIds)
    }

    @Test
    fun `비활성화 된 Question을 포함한 주문을 생성할 수 없다`() {
        // given
        val user = userRepository.save(
            Fixture.fixtureMonkey.giveMeKotlinBuilder<User>()
                .set(User::uid, null)
                .build()
                .sample()
        )

        val availableQuestionIds = Fixture.fixtureMonkey.giveMeKotlinBuilder<Question>()
            .set(Question::id, null)
            .set(Question::questionStatus, QuestionStatus.Available)
            .sampleList(10)
            .stream()
            .map { question ->
                questionRepository.save(question).id
            }
            .toList()

        val unavailableQuestionIds = Fixture.fixtureMonkey.giveMeKotlinBuilder<Question>()
            .set(Question::id, null)
            .set(Question::questionStatus, QuestionStatus.UnAvailable)
            .sampleList(10)
            .stream()
            .map { question ->
                questionRepository.save(question).id
            }
            .toList()

        val questionIds: MutableList<Long> = mutableListOf()
        questionIds.addAll(availableQuestionIds)
        questionIds.addAll(unavailableQuestionIds)

        //when then
        Assertions.assertThatThrownBy {
            questionOrderGenerator.generateQuestionOrder(
                user.uid, questionIds
            )
        }
            .isInstanceOf(CoreException::class.java)
            .hasFieldOrPropertyWithValue("error", Error.UNAVAILABLE_QUESTION)
    }

    @Test
    fun `이미 구매한 Question은 주문에 포함할 수 없다`() {
        // given
        val user = userRepository.save(
            Fixture.fixtureMonkey.giveMeKotlinBuilder<User>()
                .set(User::uid, null)
                .build()
                .sample()
        )

        val alreadyOwnedQuestion = questionRepository.save(
            Fixture.fixtureMonkey.giveMeKotlinBuilder<Question>()
                .set(Question::id, null)
                .set(Question::questionStatus, QuestionStatus.Available)
                .sample()
        )

        val normalQuestion = questionRepository.save(
            Fixture.fixtureMonkey.giveMeKotlinBuilder<Question>()
                .set(Question::id, null)
                .set(Question::questionStatus, QuestionStatus.Available)
                .sample()
        )

        userQuestionRepository.saveAll(create(user.uid, listOf(alreadyOwnedQuestion.id)))

        // when then
        Assertions.assertThatThrownBy {
            questionOrderGenerator.generateQuestionOrder(
                user.uid,
                listOf(normalQuestion.id, alreadyOwnedQuestion.id)
            )
        }
            .isInstanceOf(CoreException::class.java)
            .hasFieldOrPropertyWithValue("error", Error.ALREADY_OWN_QUESTION)
    }
}