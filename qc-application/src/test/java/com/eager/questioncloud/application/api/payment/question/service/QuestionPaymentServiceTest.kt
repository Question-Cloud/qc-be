package com.eager.questioncloud.application.api.payment.question.service

import com.eager.questioncloud.application.utils.Fixture
import com.eager.questioncloud.application.utils.UserFixtureHelper
import com.eager.questioncloud.core.domain.coupon.infrastructure.repository.CouponRepository
import com.eager.questioncloud.core.domain.coupon.infrastructure.repository.UserCouponRepository
import com.eager.questioncloud.core.domain.payment.enums.QuestionPaymentStatus
import com.eager.questioncloud.core.domain.payment.infrastructure.repository.QuestionOrderRepository
import com.eager.questioncloud.core.domain.payment.model.QuestionOrder.Companion.createOrder
import com.eager.questioncloud.core.domain.point.infrastructure.repository.UserPointRepository
import com.eager.questioncloud.core.domain.point.model.UserPoint
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
import org.springframework.test.context.event.RecordApplicationEvents

@SpringBootTest
@RecordApplicationEvents
@ActiveProfiles("test")
internal class QuestionPaymentServiceTest(
    @Autowired val questionPaymentService: QuestionPaymentService,
    @Autowired val userRepository: UserRepository,
    @Autowired val questionRepository: QuestionRepository,
    @Autowired val couponRepository: CouponRepository,
    @Autowired val userCouponRepository: UserCouponRepository,
    @Autowired val userPointRepository: UserPointRepository,
    @Autowired val questionOrderRepository: QuestionOrderRepository,
) {
    private var uid: Long = 0

    @BeforeEach
    fun setUp() {
        uid = UserFixtureHelper.createDefaultEmailUser(userRepository).uid
    }

    @AfterEach
    fun tearDown() {
        userRepository.deleteAllInBatch()
        questionRepository.deleteAllInBatch()
        couponRepository.deleteAllInBatch()
        userCouponRepository.deleteAllInBatch()
        questionOrderRepository.deleteAllInBatch()
        userPointRepository.deleteAllInBatch()
    }

    @Test
    fun `문제 결제를 할 수 있다`() {
        // given
        userPointRepository.save(UserPoint(uid, 1000000))

        val questions = Fixture.fixtureMonkey.giveMeBuilder(
            Question::class.java
        )
            .set("id", null)
            .set("creatorId", 1L)
            .set("questionContent.questionCategoryId", 25L)
            .set("questionContent.price", 1000)
            .set("questionStatus", QuestionStatus.Available)
            .sampleList(10)
            .stream()
            .map { question: Question ->
                questionRepository.save(
                    question
                )
            }
            .toList()

        // when
        val questionPayment = questionPaymentService.payment(uid, createOrder(questions), null)

        // then
        Assertions.assertThat(questionPayment.status).isEqualTo(QuestionPaymentStatus.SUCCESS)
        Assertions.assertThat(questionPayment.order.questionIds.size).isEqualTo(questions.size)
    }
}