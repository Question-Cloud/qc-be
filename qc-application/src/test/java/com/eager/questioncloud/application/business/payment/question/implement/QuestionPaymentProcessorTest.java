package com.eager.questioncloud.application.business.payment.question.implement;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.eager.questioncloud.application.utils.Fixture;
import com.eager.questioncloud.core.domain.coupon.enums.CouponType;
import com.eager.questioncloud.core.domain.coupon.infrastructure.repository.CouponRepository;
import com.eager.questioncloud.core.domain.coupon.infrastructure.repository.UserCouponRepository;
import com.eager.questioncloud.core.domain.coupon.model.Coupon;
import com.eager.questioncloud.core.domain.coupon.model.UserCoupon;
import com.eager.questioncloud.core.domain.payment.infrastructure.repository.QuestionOrderRepository;
import com.eager.questioncloud.core.domain.payment.infrastructure.repository.QuestionPaymentRepository;
import com.eager.questioncloud.core.domain.payment.model.QuestionOrder;
import com.eager.questioncloud.core.domain.payment.model.QuestionPayment;
import com.eager.questioncloud.core.domain.payment.model.QuestionPaymentCoupon;
import com.eager.questioncloud.core.domain.point.implement.UserPointManager;
import com.eager.questioncloud.core.domain.point.infrastructure.repository.UserPointRepository;
import com.eager.questioncloud.core.domain.point.model.UserPoint;
import com.eager.questioncloud.core.domain.question.enums.QuestionStatus;
import com.eager.questioncloud.core.domain.question.infrastructure.repository.QuestionRepository;
import com.eager.questioncloud.core.domain.question.model.Question;
import com.eager.questioncloud.core.domain.user.infrastructure.repository.UserRepository;
import com.eager.questioncloud.core.domain.user.model.User;
import com.eager.questioncloud.core.exception.CoreException;
import com.eager.questioncloud.core.exception.Error;
import java.time.LocalDateTime;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class QuestionPaymentProcessorTest {
    @Autowired
    QuestionPaymentProcessor questionPaymentProcessor;

    @Autowired
    UserRepository userRepository;

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    CouponRepository couponRepository;

    @Autowired
    UserCouponRepository userCouponRepository;

    @Autowired
    UserPointRepository userPointRepository;

    @SpyBean
    QuestionOrderRepository questionOrderRepository;

    @SpyBean
    QuestionPaymentRepository questionPaymentRepository;

    @SpyBean
    QuestionPaymentCouponProcessor questionPaymentCouponProcessor;

    @SpyBean
    UserPointManager userPointManager;

    @SpyBean
    QuestionPaymentHistoryGenerator questionPaymentHistoryGenerator;

    @AfterEach
    void tearDown() {
        userRepository.deleteAllInBatch();
        questionRepository.deleteAllInBatch();
        couponRepository.deleteAllInBatch();
        userCouponRepository.deleteAllInBatch();
        questionOrderRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("문제 결제 처리를 할 수 있다. (쿠폰 O)")
    void processQuestionPayment() {
        // given
        User user = userRepository.save(Fixture.fixtureMonkey.giveMeBuilder(User.class)
            .set("uid", null)
            .sample()
        );

        int beforeUserPoint = 1000000;
        userPointRepository.save(new UserPoint(user.getUid(), beforeUserPoint));

        Coupon coupon = couponRepository.save(
            Fixture.fixtureMonkey.giveMeBuilder(Coupon.class)
                .set("id", null)
                .set("couponType", CouponType.Fixed)
                .set("value", 1000)
                .set("endAt", LocalDateTime.now().plusDays(10))
                .sample()
        );

        UserCoupon userCoupon = userCouponRepository.save(
            Fixture.fixtureMonkey.giveMeBuilder(UserCoupon.class)
                .set("couponId", coupon.getId())
                .set("userId", user.getUid())
                .set("endAt", coupon.getEndAt())
                .set("isUsed", false)
                .sample()
        );

        List<Question> questions = Fixture.fixtureMonkey.giveMeBuilder(Question.class)
            .set("id", null)
            .set("questionContent.questionCategoryId", 25L)
            .set("questionContent.price", 1000)
            .set("questionStatus", QuestionStatus.Available)
            .sampleList(10);

        QuestionPayment questionPayment = QuestionPayment.create(
            user.getUid(),
            QuestionPaymentCoupon.create(userCoupon.getId(), coupon),
            QuestionOrder.createOrder(questions)
        );

        int originalAmount = questionPayment.getAmount();
        int discountedAmount = questionPayment.getQuestionPaymentCoupon().calcDiscount(originalAmount);

        // when
        QuestionPayment paymentResult = questionPaymentProcessor.processQuestionPayment(questionPayment);

        // then
        verify(questionPaymentCouponProcessor, times(1)).applyCoupon(questionPayment);
        verify(userPointManager, times(1)).usePoint(questionPayment.getUserId(), questionPayment.getAmount());
        verify(questionOrderRepository, times(1)).save(questionPayment.getOrder());
        verify(questionPaymentRepository, times(1)).save(questionPayment);
        verify(questionPaymentHistoryGenerator, times(1)).saveQuestionPaymentHistory(paymentResult);

        UserCoupon afterUserCoupon = userCouponRepository.getUserCoupon(userCoupon.getId());
        Assertions.assertThat(afterUserCoupon.getIsUsed()).isTrue();

        Assertions.assertThat(paymentResult.getAmount()).isEqualTo(discountedAmount);

        UserPoint afterUserPoint = userPointRepository.getUserPoint(user.getUid());
        Assertions.assertThat(afterUserPoint.getPoint()).isEqualTo(beforeUserPoint - paymentResult.getAmount());
    }

    @Test
    @DisplayName("문제 결제 처리를 할 수 있다. (쿠폰 X)")
    void processQuestionPaymentNoCoupon() {
        // given
        User user = userRepository.save(Fixture.fixtureMonkey.giveMeBuilder(User.class)
            .set("uid", null)
            .sample()
        );

        int beforeUserPoint = 1000000;
        userPointRepository.save(new UserPoint(user.getUid(), beforeUserPoint));

        List<Question> questions = Fixture.fixtureMonkey.giveMeBuilder(Question.class)
            .set("id", null)
            .set("questionContent.questionCategoryId", 25L)
            .set("questionContent.price", 1000)
            .set("questionStatus", QuestionStatus.Available)
            .sampleList(10);

        QuestionPayment questionPayment = QuestionPayment.create(
            user.getUid(),
            QuestionPaymentCoupon.noCoupon(),
            QuestionOrder.createOrder(questions)
        );

        int originalAmount = questionPayment.getAmount();

        // when
        QuestionPayment paymentResult = questionPaymentProcessor.processQuestionPayment(questionPayment);

        // then
        verify(questionPaymentCouponProcessor, times(1)).applyCoupon(questionPayment);
        verify(userPointManager, times(1)).usePoint(questionPayment.getUserId(), questionPayment.getAmount());
        verify(questionOrderRepository, times(1)).save(questionPayment.getOrder());
        verify(questionPaymentRepository, times(1)).save(questionPayment);
        verify(questionPaymentHistoryGenerator, times(1)).saveQuestionPaymentHistory(paymentResult);

        Assertions.assertThat(paymentResult.getAmount()).isEqualTo(originalAmount);

        UserPoint afterUserPoint = userPointRepository.getUserPoint(user.getUid());
        Assertions.assertThat(afterUserPoint.getPoint()).isEqualTo(beforeUserPoint - paymentResult.getAmount());
    }

    @Test
    @DisplayName("보유 포인트가 부족하면 포인트 부족 예외가 발생한다.")
    void cancelQuestionPaymentWhenNotEnoughUserPoint() {
        // given
        User user = userRepository.save(Fixture.fixtureMonkey.giveMeBuilder(User.class)
            .set("uid", null)
            .sample()
        );

        int beforeUserPoint = 1000;
        userPointRepository.save(new UserPoint(user.getUid(), beforeUserPoint));

        Coupon coupon = couponRepository.save(
            Fixture.fixtureMonkey.giveMeBuilder(Coupon.class)
                .set("id", null)
                .set("couponType", CouponType.Fixed)
                .set("value", 1000)
                .set("endAt", LocalDateTime.now().plusDays(10))
                .sample()
        );

        UserCoupon userCoupon = userCouponRepository.save(
            Fixture.fixtureMonkey.giveMeBuilder(UserCoupon.class)
                .set("couponId", coupon.getId())
                .set("userId", user.getUid())
                .set("endAt", coupon.getEndAt())
                .set("isUsed", false)
                .sample()
        );

        List<Question> questions = Fixture.fixtureMonkey.giveMeBuilder(Question.class)
            .set("id", null)
            .set("questionContent.questionCategoryId", 25L)
            .set("questionContent.price", 1000)
            .set("questionStatus", QuestionStatus.Available)
            .sampleList(10);

        QuestionPayment questionPayment = QuestionPayment.create(
            user.getUid(),
            QuestionPaymentCoupon.create(userCoupon.getId(), coupon),
            QuestionOrder.createOrder(questions)
        );

        // when then
        Assertions.assertThatThrownBy(() -> questionPaymentProcessor.processQuestionPayment(questionPayment))
            .isInstanceOf(CoreException.class)
            .hasFieldOrPropertyWithValue("error", Error.NOT_ENOUGH_POINT);
    }

    @Test
    @DisplayName("결제 도중 예외가 발생하면 쿠폰, 포인트가 롤백 처리 된다.")
    void rollbackWhenOccurException() {
        // given
        User user = userRepository.save(Fixture.fixtureMonkey.giveMeBuilder(User.class)
            .set("uid", null)
            .sample()
        );

        int beforeUserPoint = 1000000;
        userPointRepository.save(new UserPoint(user.getUid(), beforeUserPoint));

        Coupon coupon = couponRepository.save(
            Fixture.fixtureMonkey.giveMeBuilder(Coupon.class)
                .set("id", null)
                .set("couponType", CouponType.Fixed)
                .set("value", 1000)
                .set("endAt", LocalDateTime.now().plusDays(10))
                .sample()
        );

        UserCoupon userCoupon = userCouponRepository.save(
            Fixture.fixtureMonkey.giveMeBuilder(UserCoupon.class)
                .set("couponId", coupon.getId())
                .set("userId", user.getUid())
                .set("endAt", coupon.getEndAt())
                .set("isUsed", false)
                .sample()
        );

        List<Question> questions = Fixture.fixtureMonkey.giveMeBuilder(Question.class)
            .set("id", null)
            .set("questionContent.questionCategoryId", 25L)
            .set("questionContent.price", 1000)
            .set("questionStatus", QuestionStatus.Available)
            .sampleList(10);

        QuestionPayment questionPayment = QuestionPayment.create(
            user.getUid(),
            QuestionPaymentCoupon.create(userCoupon.getId(), coupon),
            QuestionOrder.createOrder(questions)
        );

        Mockito.doThrow(new CoreException(Error.PAYMENT_ERROR)).when(questionPaymentRepository).save(any());

        // when then
        Assertions.assertThatThrownBy(() -> questionPaymentProcessor.processQuestionPayment(questionPayment))
            .isInstanceOf(CoreException.class);

        UserCoupon afterUserCoupon = userCouponRepository.getUserCoupon(userCoupon.getId());
        Assertions.assertThat(afterUserCoupon.getIsUsed()).isFalse();

        UserPoint afterUserPoint = userPointRepository.getUserPoint(user.getUid());
        Assertions.assertThat(afterUserPoint.getPoint()).isEqualTo(beforeUserPoint);
    }
}