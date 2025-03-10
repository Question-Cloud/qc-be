package com.eager.questioncloud.application.business.payment.question.implement

import com.eager.questioncloud.application.utils.Fixture
import com.eager.questioncloud.core.domain.coupon.enums.CouponType
import com.eager.questioncloud.core.domain.coupon.infrastructure.repository.CouponRepository
import com.eager.questioncloud.core.domain.coupon.infrastructure.repository.UserCouponRepository
import com.eager.questioncloud.core.domain.coupon.model.Coupon
import com.eager.questioncloud.core.domain.coupon.model.UserCoupon
import com.eager.questioncloud.core.domain.payment.infrastructure.repository.QuestionOrderRepository
import com.eager.questioncloud.core.domain.payment.infrastructure.repository.QuestionPaymentRepository
import com.eager.questioncloud.core.domain.payment.model.QuestionOrder.Companion.createOrder
import com.eager.questioncloud.core.domain.payment.model.QuestionPayment.Companion.create
import com.eager.questioncloud.core.domain.payment.model.QuestionPaymentCoupon.Companion.create
import com.eager.questioncloud.core.domain.point.implement.UserPointManager
import com.eager.questioncloud.core.domain.point.infrastructure.repository.UserPointRepository
import com.eager.questioncloud.core.domain.point.model.UserPoint
import com.eager.questioncloud.core.domain.question.enums.QuestionStatus
import com.eager.questioncloud.core.domain.question.infrastructure.repository.QuestionRepository
import com.eager.questioncloud.core.domain.question.model.Question
import com.eager.questioncloud.core.domain.question.model.QuestionContent
import com.eager.questioncloud.core.domain.user.infrastructure.repository.UserRepository
import com.eager.questioncloud.core.domain.user.model.User
import com.eager.questioncloud.core.exception.CoreException
import com.eager.questioncloud.core.exception.Error
import com.navercorp.fixturemonkey.kotlin.giveMeKotlinBuilder
import com.navercorp.fixturemonkey.kotlin.into
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.SpyBean
import org.springframework.test.context.ActiveProfiles
import java.time.LocalDateTime

@SpringBootTest
@ActiveProfiles("test")
internal class QuestionPaymentProcessorTest {
    @Autowired
    var questionPaymentProcessor: QuestionPaymentProcessor? = null

    @Autowired
    var userRepository: UserRepository? = null

    @Autowired
    var questionRepository: QuestionRepository? = null

    @Autowired
    var couponRepository: CouponRepository? = null

    @Autowired
    var userCouponRepository: UserCouponRepository? = null

    @Autowired
    var userPointRepository: UserPointRepository? = null

    @SpyBean
    var questionOrderRepository: QuestionOrderRepository? = null

    @SpyBean
    var questionPaymentRepository: QuestionPaymentRepository? = null

    @SpyBean
    var questionPaymentCouponProcessor: QuestionPaymentCouponProcessor? = null

    @SpyBean
    var userPointManager: UserPointManager? = null

    @SpyBean
    var questionPaymentHistoryRegister: QuestionPaymentHistoryRegister? = null

    @AfterEach
    fun tearDown() {
        userRepository!!.deleteAllInBatch()
        questionRepository!!.deleteAllInBatch()
        couponRepository!!.deleteAllInBatch()
        userCouponRepository!!.deleteAllInBatch()
        questionOrderRepository!!.deleteAllInBatch()
    }

    @Test
    @DisplayName("문제 결제 처리를 할 수 있다. (쿠폰 O)")
    fun payment() {
        // given
        val user = userRepository!!.save(
            Fixture.fixtureMonkey.giveMeKotlinBuilder<User>()
                .set(User::uid, null)
                .build()
                .sample()
        )

        val beforeUserPoint = 1000000
        userPointRepository!!.save(UserPoint(user.uid!!, beforeUserPoint))

        val coupon = couponRepository!!.save(
            Fixture.fixtureMonkey.giveMeKotlinBuilder<Coupon>()
                .set(Coupon::id, null)
                .set(Coupon::value, 1000)
                .set(Coupon::couponType, CouponType.Fixed)
                .set(Coupon::endAt, LocalDateTime.now().plusDays(10))
                .sample()
        )

        val userCoupon = userCouponRepository!!.save(
            Fixture.fixtureMonkey.giveMeKotlinBuilder<UserCoupon>()
                .set(UserCoupon::couponId, coupon.id)
                .set(UserCoupon::userId, user.uid)
                .set(UserCoupon::endAt, coupon.endAt)
                .set(UserCoupon::isUsed, false)
                .sample()
        )

        val questions = Fixture.fixtureMonkey.giveMeKotlinBuilder<Question>()
            .set(Question::id, null)
            .set(Question::questionContent into QuestionContent::questionCategoryId, 25L)
            .set(Question::questionContent into QuestionContent::price, 1000)
            .set(Question::questionStatus, QuestionStatus.Available)
            .sampleList(10)
            .stream()
            .map { question ->
                questionRepository!!.save(question)
            }
            .toList()

        val questionPayment = create(
            user.uid!!,
            create(userCoupon.id!!, coupon),
            createOrder(questions)
        )

        val originalAmount = questionPayment.amount
        val discountedAmount = questionPayment.questionPaymentCoupon!!.calcDiscount(originalAmount)

        // when
        val paymentResult = questionPaymentProcessor!!.payment(questionPayment)

        // then
        Mockito.verify(questionPaymentCouponProcessor, Mockito.times(1))!!.applyCoupon(questionPayment)
        Mockito.verify(userPointManager, Mockito.times(1))!!.usePoint(questionPayment.userId, questionPayment.amount)
        Mockito.verify(questionPaymentRepository, Mockito.times(1))!!.save(questionPayment)

        val afterUserCoupon = userCouponRepository!!.getUserCoupon(userCoupon.id!!)
        Assertions.assertThat(afterUserCoupon.isUsed).isTrue()

        Assertions.assertThat(questionPayment.amount).isEqualTo(discountedAmount)

        val afterUserPoint = userPointRepository!!.getUserPoint(user.uid!!)
        Assertions.assertThat(afterUserPoint.point).isEqualTo(beforeUserPoint - questionPayment.amount)
    }

    @Test
    @DisplayName("문제 결제 처리를 할 수 있다. (쿠폰 X)")
    fun paymentNoCoupon() {
        // given
        val user = userRepository!!.save(
            Fixture.fixtureMonkey.giveMeKotlinBuilder<User>()
                .set(User::uid, null)
                .build()
                .sample()
        )

        val beforeUserPoint = 1000000
        userPointRepository!!.save(UserPoint(user.uid!!, beforeUserPoint))

        val questions = Fixture.fixtureMonkey.giveMeKotlinBuilder<Question>()
            .set(Question::id, null)
            .set(Question::questionContent into QuestionContent::questionCategoryId, 25L)
            .set(Question::questionContent into QuestionContent::price, 1000)
            .set(Question::questionStatus, QuestionStatus.Available)
            .sampleList(10)
            .stream()
            .map { question ->
                questionRepository!!.save(question)
            }
            .toList()

        val questionPayment = create(
            user.uid!!,
            null,
            createOrder(questions)
        )

        val originalAmount = questionPayment.amount

        // when
        val paymentResult = questionPaymentProcessor!!.payment(questionPayment)

        // then
        Mockito.verify(questionPaymentCouponProcessor, Mockito.times(1))!!.applyCoupon(questionPayment)
        Mockito.verify(userPointManager, Mockito.times(1))!!.usePoint(questionPayment.userId, questionPayment.amount)
        Mockito.verify(questionPaymentRepository, Mockito.times(1))!!.save(questionPayment)

        Assertions.assertThat(questionPayment.amount).isEqualTo(originalAmount)

        val afterUserPoint = userPointRepository!!.getUserPoint(user.uid!!)
        Assertions.assertThat(afterUserPoint.point).isEqualTo(beforeUserPoint - questionPayment.amount)
    }

    @Test
    @DisplayName("보유 포인트가 부족하면 포인트 부족 예외가 발생한다.")
    fun cancelQuestionPaymentWhenNotEnoughUserPoint() {
        // given
        val user = userRepository!!.save(
            Fixture.fixtureMonkey.giveMeKotlinBuilder<User>()
                .set(User::uid, null)
                .build()
                .sample()
        )

        val beforeUserPoint = 1000
        userPointRepository!!.save(UserPoint(user.uid!!, beforeUserPoint))

        val coupon = couponRepository!!.save(
            Fixture.fixtureMonkey.giveMeKotlinBuilder<Coupon>()
                .set(Coupon::id, null)
                .set(Coupon::value, 1000)
                .set(Coupon::couponType, CouponType.Fixed)
                .set(Coupon::endAt, LocalDateTime.now().plusDays(10))
                .sample()
        )

        val userCoupon = userCouponRepository!!.save(
            Fixture.fixtureMonkey.giveMeKotlinBuilder<UserCoupon>()
                .set(UserCoupon::couponId, coupon.id)
                .set(UserCoupon::userId, user.uid)
                .set(UserCoupon::endAt, coupon.endAt)
                .set(UserCoupon::isUsed, false)
                .sample()
        )

        val questions = Fixture.fixtureMonkey.giveMeKotlinBuilder<Question>()
            .set(Question::id, null)
            .set(Question::questionContent into QuestionContent::questionCategoryId, 25L)
            .set(Question::questionContent into QuestionContent::price, 1000)
            .set(Question::questionStatus, QuestionStatus.Available)
            .sampleList(10)
            .stream()
            .map { question ->
                questionRepository!!.save(question)
            }
            .toList()

        val questionPayment = create(
            user.uid!!,
            create(userCoupon.id!!, coupon),
            createOrder(questions)
        )

        // when then
        Assertions.assertThatThrownBy { questionPaymentProcessor!!.payment(questionPayment) }
            .isInstanceOf(CoreException::class.java)
            .hasFieldOrPropertyWithValue("error", Error.NOT_ENOUGH_POINT)
    }

    @Test
    @DisplayName("결제 도중 예외가 발생하면 쿠폰, 포인트가 롤백 처리 된다.")
    fun rollbackWhenOccurException() {
        // given
        val user = userRepository!!.save(
            Fixture.fixtureMonkey.giveMeKotlinBuilder<User>()
                .set(User::uid, null)
                .build()
                .sample()
        )

        val beforeUserPoint = 1000000
        userPointRepository!!.save(UserPoint(user.uid!!, beforeUserPoint))

        val coupon = couponRepository!!.save(
            Fixture.fixtureMonkey.giveMeKotlinBuilder<Coupon>()
                .set(Coupon::id, null)
                .set(Coupon::value, 1000)
                .set(Coupon::couponType, CouponType.Fixed)
                .set(Coupon::endAt, LocalDateTime.now().plusDays(10))
                .sample()
        )

        val userCoupon = userCouponRepository!!.save(
            Fixture.fixtureMonkey.giveMeKotlinBuilder<UserCoupon>()
                .set(UserCoupon::couponId, coupon.id)
                .set(UserCoupon::userId, user.uid)
                .set(UserCoupon::endAt, coupon.endAt)
                .set(UserCoupon::isUsed, false)
                .sample()
        )

        val questions = Fixture.fixtureMonkey.giveMeKotlinBuilder<Question>()
            .set(Question::id, null)
            .set(Question::questionContent into QuestionContent::questionCategoryId, 25L)
            .set(Question::questionContent into QuestionContent::price, 1000)
            .set(Question::questionStatus, QuestionStatus.Available)
            .sampleList(10)
            .stream()
            .map { question ->
                questionRepository!!.save(question)
            }
            .toList()

        val questionPayment = create(
            user.uid!!,
            create(userCoupon.id!!, coupon),
            createOrder(questions)
        )

        Mockito.doThrow(CoreException(Error.PAYMENT_ERROR)).`when`(questionPaymentRepository)!!
            .save(any())

        // when then
        Assertions.assertThatThrownBy { questionPaymentProcessor!!.payment(questionPayment) }
            .isInstanceOf(CoreException::class.java)

        val afterUserCoupon = userCouponRepository!!.getUserCoupon(userCoupon.id!!)
        Assertions.assertThat(afterUserCoupon.isUsed).isFalse()

        val afterUserPoint = userPointRepository!!.getUserPoint(user.uid!!)
        Assertions.assertThat(afterUserPoint.point).isEqualTo(beforeUserPoint)
    }
}