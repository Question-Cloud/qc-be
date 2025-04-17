package com.eager.questioncloud.application.api.payment.question.implement

import com.eager.questioncloud.application.utils.fixture.Fixture
import com.eager.questioncloud.application.utils.fixture.helper.*
import com.eager.questioncloud.core.domain.coupon.infrastructure.repository.CouponRepository
import com.eager.questioncloud.core.domain.coupon.infrastructure.repository.UserCouponRepository
import com.eager.questioncloud.core.domain.creator.infrastructure.repository.CreatorRepository
import com.eager.questioncloud.core.domain.payment.infrastructure.repository.QuestionOrderRepository
import com.eager.questioncloud.core.domain.payment.infrastructure.repository.QuestionPaymentRepository
import com.eager.questioncloud.core.domain.payment.model.QuestionOrder.Companion.createOrder
import com.eager.questioncloud.core.domain.payment.model.QuestionPayment.Companion.create
import com.eager.questioncloud.core.domain.payment.model.QuestionPaymentCoupon.Companion.create
import com.eager.questioncloud.core.domain.point.implement.UserPointManager
import com.eager.questioncloud.core.domain.point.infrastructure.repository.UserPointRepository
import com.eager.questioncloud.core.domain.question.enums.QuestionStatus
import com.eager.questioncloud.core.domain.question.infrastructure.repository.QuestionRepository
import com.eager.questioncloud.core.domain.question.model.Question
import com.eager.questioncloud.core.domain.question.model.QuestionContent
import com.eager.questioncloud.core.domain.user.infrastructure.repository.UserRepository
import com.eager.questioncloud.core.exception.CoreException
import com.eager.questioncloud.core.exception.Error
import com.navercorp.fixturemonkey.kotlin.giveMeKotlinBuilder
import com.navercorp.fixturemonkey.kotlin.into
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.SpyBean
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
internal class QuestionPaymentProcessorTest(
    @Autowired val questionPaymentProcessor: QuestionPaymentProcessor,
    @Autowired val userRepository: UserRepository,
    @Autowired val questionRepository: QuestionRepository,
    @Autowired val couponRepository: CouponRepository,
    @Autowired val userCouponRepository: UserCouponRepository,
    @Autowired val userPointRepository: UserPointRepository,
    @Autowired val creatorRepository: CreatorRepository,
    @Autowired @SpyBean val questionOrderRepository: QuestionOrderRepository,
    @Autowired @SpyBean val questionPaymentRepository: QuestionPaymentRepository,
    @Autowired @SpyBean val questionPaymentCouponProcessor: QuestionPaymentCouponProcessor,
    @Autowired @SpyBean val userPointManager: UserPointManager,
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
        userRepository.deleteAllInBatch()
        creatorRepository.deleteAllInBatch()
        questionRepository.deleteAllInBatch()
        couponRepository.deleteAllInBatch()
        userCouponRepository.deleteAllInBatch()
        questionOrderRepository.deleteAllInBatch()
    }

    @Test
    fun `문제 결제 처리를 할 수 있다 (쿠폰 O)`() {
        // given
        val beforeUserPoint = 1000000
        UserPointFixtureHelper.createUserPoint(uid, beforeUserPoint, userPointRepository)

        val coupon = CouponFixtureHelper.createCoupon(couponRepository = couponRepository)
        val userCoupon =
            UserCouponFixtureHelper.createUserCoupon(
                coupon = coupon,
                uid = uid,
                userCouponRepository = userCouponRepository
            )

        val questions = Fixture.fixtureMonkey.giveMeKotlinBuilder<Question>()
            .set(Question::id, null)
            .set(Question::questionContent into QuestionContent::questionCategoryId, 25L)
            .set(Question::questionContent into QuestionContent::price, 1000)
            .set(Question::questionStatus, QuestionStatus.Available)
            .sampleList(10)
            .stream()
            .map { question ->
                questionRepository.save(question)
            }
            .toList()

        val questionPayment = create(
            uid,
            create(userCoupon.id, coupon),
            createOrder(questions)
        )

        val originalAmount = questionPayment.amount
        val discountedAmount = questionPayment.questionPaymentCoupon!!.calcDiscount(originalAmount)

        // when
        val paymentResult = questionPaymentProcessor.payment(questionPayment)

        // then
        Mockito.verify(questionPaymentCouponProcessor, Mockito.times(1)).applyCoupon(questionPayment)
        Mockito.verify(userPointManager, Mockito.times(1)).usePoint(questionPayment.userId, questionPayment.amount)
        Mockito.verify(questionPaymentRepository, Mockito.times(1)).save(questionPayment)

        val afterUserCoupon = userCouponRepository.getUserCoupon(userCoupon.id)
        Assertions.assertThat(afterUserCoupon.isUsed).isTrue()

        Assertions.assertThat(questionPayment.amount).isEqualTo(discountedAmount)

        val afterUserPoint = userPointRepository.getUserPoint(uid)
        Assertions.assertThat(afterUserPoint.point).isEqualTo(beforeUserPoint - questionPayment.amount)
    }

    @Test
    fun `문제 결제 처리를 할 수 있다 (쿠폰 X)`() {
        // given
        val beforeUserPoint = 1000000
        UserPointFixtureHelper.createUserPoint(uid, beforeUserPoint, userPointRepository)

        val questions = dummyQuestions()

        val questionPayment = create(
            uid,
            null,
            createOrder(questions)
        )

        val originalAmount = questionPayment.amount

        // when
        questionPaymentProcessor.payment(questionPayment)

        // then
        Mockito.verify(questionPaymentCouponProcessor, Mockito.times(1)).applyCoupon(questionPayment)
        Mockito.verify(userPointManager, Mockito.times(1)).usePoint(questionPayment.userId, questionPayment.amount)
        Mockito.verify(questionPaymentRepository, Mockito.times(1)).save(questionPayment)

        Assertions.assertThat(questionPayment.amount).isEqualTo(originalAmount)

        val afterUserPoint = userPointRepository.getUserPoint(uid)
        Assertions.assertThat(afterUserPoint.point).isEqualTo(beforeUserPoint - questionPayment.amount)
    }

    @Test
    fun `보유 포인트가 부족하면 포인트 부족 예외가 발생한다`() {
        // given
        val beforeUserPoint = 1000
        UserPointFixtureHelper.createUserPoint(uid, beforeUserPoint, userPointRepository)

        val coupon = CouponFixtureHelper.createCoupon(couponRepository = couponRepository)
        val userCoupon = UserCouponFixtureHelper.createUserCoupon(
            coupon = coupon,
            uid = uid,
            userCouponRepository = userCouponRepository
        )

        val questions = dummyQuestions()

        val questionPayment = create(
            uid,
            create(userCoupon.id, coupon),
            createOrder(questions)
        )

        // when then
        Assertions.assertThatThrownBy { questionPaymentProcessor.payment(questionPayment) }
            .isInstanceOf(CoreException::class.java)
            .hasFieldOrPropertyWithValue("error", Error.NOT_ENOUGH_POINT)
    }

    @Test
    fun `결제 도중 예외가 발생하면 쿠폰, 포인트가 롤백 처리 된다`() {
        // given
        val beforeUserPoint = 1000000
        UserPointFixtureHelper.createUserPoint(uid, beforeUserPoint, userPointRepository)

        val coupon = CouponFixtureHelper.createCoupon(couponRepository = couponRepository)
        val userCoupon = UserCouponFixtureHelper.createUserCoupon(
            coupon = coupon,
            uid = uid,
            userCouponRepository = userCouponRepository
        )

        val questions = dummyQuestions()

        val questionPayment = create(
            uid,
            create(userCoupon.id, coupon),
            createOrder(questions)
        )

        Mockito.doThrow(CoreException(Error.PAYMENT_ERROR)).`when`(questionPaymentRepository)
            .save(any())

        // when then
        Assertions.assertThatThrownBy { questionPaymentProcessor.payment(questionPayment) }
            .isInstanceOf(CoreException::class.java)

        val afterUserCoupon = userCouponRepository.getUserCoupon(userCoupon.id)
        Assertions.assertThat(afterUserCoupon.isUsed).isFalse()

        val afterUserPoint = userPointRepository.getUserPoint(uid)
        Assertions.assertThat(afterUserPoint.point).isEqualTo(beforeUserPoint)
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