package com.eager.questioncloud.application.api.payment.question.implement

import com.eager.questioncloud.application.utils.DBCleaner
import com.eager.questioncloud.application.utils.fixture.helper.CreatorFixtureHelper
import com.eager.questioncloud.application.utils.fixture.helper.QuestionFixtureHelper
import com.eager.questioncloud.application.utils.fixture.helper.UserFixtureHelper
import com.eager.questioncloud.core.domain.creator.infrastructure.repository.CreatorRepository
import com.eager.questioncloud.core.domain.question.enums.QuestionStatus
import com.eager.questioncloud.core.domain.question.infrastructure.repository.QuestionRepository
import com.eager.questioncloud.core.domain.question.model.Question
import com.eager.questioncloud.core.domain.user.infrastructure.repository.UserRepository
import com.eager.questioncloud.core.domain.userquestion.infrastructure.repository.UserQuestionRepository
import com.eager.questioncloud.core.domain.userquestion.model.UserQuestion.Companion.create
import com.eager.questioncloud.core.exception.CoreException
import com.eager.questioncloud.core.exception.Error
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
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
    @Autowired val creatorRepository: CreatorRepository,
    @Autowired val questionOrderGenerator: QuestionOrderGenerator,
    @Autowired val dbCleaner: DBCleaner,
) {
    private var uid: Long = 0
    private var creatorId: Long = 0

    @BeforeEach
    fun setUp() {
        uid = UserFixtureHelper.createDefaultEmailUser(userRepository).uid
        creatorId = CreatorFixtureHelper.createCreator(uid, creatorRepository).id
    }

    @AfterEach
    fun tearDown() {
        dbCleaner.cleanUp()
    }

    @Test
    fun `Question 주문을 생성할 수 있다`() {
        // given
        val questionIds = dummyQuestionIds()

        // when
        val questionOrder = questionOrderGenerator.generateQuestionOrder(uid, questionIds)

        // then
        Assertions.assertThat(questionOrder.questionIds).containsExactlyInAnyOrderElementsOf(questionIds)
    }

    @Test
    fun `비활성화 된 Question을 포함한 주문을 생성할 수 없다`() {
        // given
        val availableQuestionIds = dummyQuestionIds()

        val unavailableQuestionId = createDummyQuestion(questionStatus = QuestionStatus.UnAvailable).id

        val questionIds: MutableList<Long> = mutableListOf()
        questionIds.addAll(availableQuestionIds)
        questionIds.add(unavailableQuestionId)

        //when then
        Assertions.assertThatThrownBy {
            questionOrderGenerator.generateQuestionOrder(
                uid, questionIds
            )
        }
            .isInstanceOf(CoreException::class.java)
            .hasFieldOrPropertyWithValue("error", Error.UNAVAILABLE_QUESTION)
    }

    @Test
    fun `이미 구매한 Question은 주문에 포함할 수 없다`() {
        // given
        val alreadyOwnedQuestion = createDummyQuestion()

        val normalQuestion = createDummyQuestion()

        userQuestionRepository.saveAll(create(uid, listOf(alreadyOwnedQuestion.id)))

        // when then
        Assertions.assertThatThrownBy {
            questionOrderGenerator.generateQuestionOrder(
                uid,
                listOf(normalQuestion.id, alreadyOwnedQuestion.id)
            )
        }
            .isInstanceOf(CoreException::class.java)
            .hasFieldOrPropertyWithValue("error", Error.ALREADY_OWN_QUESTION)
    }

    private fun createDummyQuestion(questionStatus: QuestionStatus = QuestionStatus.Available): Question {
        return QuestionFixtureHelper.createQuestion(
            creatorId = creatorId,
            questionStatus = questionStatus,
            questionRepository = questionRepository
        )
    }

    private fun dummyQuestionIds(): List<Long> {
        val questionIds = mutableListOf<Long>()

        for (i in 1..10) {
            val question = createDummyQuestion()
            questionIds.add(question.id)
        }

        return questionIds
    }
}