package com.eager.questioncloud.application.api.payment.question.service

import com.eager.questioncloud.application.utils.Fixture
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
import com.eager.questioncloud.core.domain.user.model.User
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.event.RecordApplicationEvents

@SpringBootTest
@RecordApplicationEvents
@ActiveProfiles("test")
internal class QuestionPaymentServiceTest {
    @Autowired
    var questionPaymentService: QuestionPaymentService? = null

    @Autowired
    private val userRepository: UserRepository? = null

    @Autowired
    private val questionRepository: QuestionRepository? = null

    @Autowired
    private val couponRepository: CouponRepository? = null

    @Autowired
    private val userCouponRepository: UserCouponRepository? = null

    @Autowired
    private val userPointRepository: UserPointRepository? = null

    @Autowired
    private val questionOrderRepository: QuestionOrderRepository? = null

    @AfterEach
    fun tearDown() {
        userRepository!!.deleteAllInBatch()
        questionRepository!!.deleteAllInBatch()
        couponRepository!!.deleteAllInBatch()
        userCouponRepository!!.deleteAllInBatch()
        questionOrderRepository!!.deleteAllInBatch()
        userPointRepository!!.deleteAllInBatch()
    }

    @Test
    @DisplayName("문제 결제를 할 수 있다.")
    fun payment() {
        // given
        val user = userRepository!!.save(
            Fixture.fixtureMonkey.giveMeBuilder(
                User::class.java
            )
                .set("uid", null)
                .sample()
        )

        userPointRepository!!.save(UserPoint(user.uid!!, 1000000))

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
            .map { question: Question? ->
                questionRepository!!.save(
                    question!!
                )
            }
            .toList()

        // when
        val questionPayment = questionPaymentService!!.payment(user.uid!!, createOrder(questions), null)

        // then
        Assertions.assertThat(questionPayment.status).isEqualTo(QuestionPaymentStatus.SUCCESS)
        Assertions.assertThat(questionPayment.order.questionIds.size).isEqualTo(questions.size)
    }
}