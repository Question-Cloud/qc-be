package com.eager.questioncloud.application.api.payment.question.service

import com.eager.questioncloud.application.utils.DBCleaner
import com.eager.questioncloud.application.utils.fixture.helper.CreatorFixtureHelper
import com.eager.questioncloud.application.utils.fixture.helper.QuestionFixtureHelper
import com.eager.questioncloud.application.utils.fixture.helper.UserFixtureHelper
import com.eager.questioncloud.application.utils.fixture.helper.UserPointFixtureHelper
import com.eager.questioncloud.core.domain.creator.infrastructure.repository.CreatorRepository
import com.eager.questioncloud.core.domain.payment.enums.QuestionPaymentStatus
import com.eager.questioncloud.core.domain.payment.model.QuestionOrder.Companion.createOrder
import com.eager.questioncloud.core.domain.point.infrastructure.repository.UserPointRepository
import com.eager.questioncloud.core.domain.question.enums.QuestionStatus
import com.eager.questioncloud.core.domain.question.infrastructure.repository.QuestionRepository
import com.eager.questioncloud.core.domain.question.model.Question
import com.eager.questioncloud.core.domain.user.infrastructure.repository.UserRepository
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
class QuestionPaymentServiceTest(
    @Autowired val questionPaymentService: QuestionPaymentService,
    @Autowired val userRepository: UserRepository,
    @Autowired val questionRepository: QuestionRepository,
    @Autowired val userPointRepository: UserPointRepository,
    @Autowired val creatorRepository: CreatorRepository,
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
    fun `문제 결제를 할 수 있다`() {
        // given
        val beforeUserPoint = 1000000
        UserPointFixtureHelper.createUserPoint(uid, beforeUserPoint, userPointRepository)

        val questions = dummyQuestions()

        // when
        val questionPayment = questionPaymentService.payment(uid, createOrder(questions), null)

        // then
        Assertions.assertThat(questionPayment.status).isEqualTo(QuestionPaymentStatus.SUCCESS)
        Assertions.assertThat(questionPayment.order.questionIds.size).isEqualTo(questions.size)
    }

    private fun createDummyQuestion(questionStatus: QuestionStatus = QuestionStatus.Available): Question {
        return QuestionFixtureHelper.createQuestion(
            creatorId = creatorId,
            questionStatus = questionStatus,
            questionRepository = questionRepository
        )
    }

    private fun dummyQuestions(): List<Question> {
        val questions = mutableListOf<Question>()

        for (i in 1..10) {
            val question = createDummyQuestion()
            questions.add(question)
        }

        return questions
    }
}