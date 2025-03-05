package com.eager.questioncloud.application.business.payment.question.implement;

import com.eager.questioncloud.application.utils.Fixture;
import com.eager.questioncloud.core.domain.coupon.infrastructure.repository.CouponRepository;
import com.eager.questioncloud.core.domain.coupon.infrastructure.repository.UserCouponRepository;
import com.eager.questioncloud.core.domain.coupon.model.Coupon;
import com.eager.questioncloud.core.domain.coupon.model.UserCoupon;
import com.eager.questioncloud.core.domain.payment.model.QuestionPaymentCoupon;
import com.eager.questioncloud.core.domain.user.infrastructure.repository.UserRepository;
import com.eager.questioncloud.core.domain.user.model.User;
import com.eager.questioncloud.core.exception.CoreException;
import com.eager.questioncloud.core.exception.Error;
import java.time.LocalDateTime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class QuestionPaymentCouponReaderTest {
    @Autowired
    UserCouponRepository userCouponRepository;

    @Autowired
    CouponRepository couponRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    QuestionPaymentCouponReader questionPaymentCouponReader;

    @AfterEach
    void tearDown() {
        userCouponRepository.deleteAllInBatch();
        couponRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("유효한 쿠폰을 불러 올 수 있다.")
    void getCoupon() {
        // given
        User user = userRepository.save(
            Fixture.fixtureMonkey.giveMeBuilder(User.class)
                .set("uid", null)
                .sample()
        );

        Coupon coupon = couponRepository.save(
            Fixture.fixtureMonkey.giveMeBuilder(Coupon.class)
                .set("id", null)
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

        // when
        QuestionPaymentCoupon questionPaymentCoupon = questionPaymentCouponReader.getQuestionPaymentCoupon(userCoupon.getId(), user.getUid());
        Assertions.assertThat(questionPaymentCoupon.getUserCouponId()).isEqualTo(userCoupon.getId());
        Assertions.assertThat(questionPaymentCoupon.getTitle()).isEqualTo(coupon.getTitle());
        Assertions.assertThat(questionPaymentCoupon.getCouponType()).isEqualTo(coupon.getCouponType());
        Assertions.assertThat(questionPaymentCoupon.getValue()).isEqualTo(coupon.getValue());
    }

    @Test
    @DisplayName("존재하지 않는 쿠폰은 불러올 수 없다.")
    void cannotGetWrongCoupon() {
        // given
        User user = userRepository.save(
            Fixture.fixtureMonkey.giveMeBuilder(User.class)
                .set("uid", null)
                .sample()
        );

        Long wrongUserCouponId = 12L;

        //when then
        Assertions.assertThatThrownBy(() -> questionPaymentCouponReader.getQuestionPaymentCoupon(wrongUserCouponId, user.getUid()))
            .isInstanceOf(CoreException.class)
            .hasFieldOrPropertyWithValue("error", Error.WRONG_COUPON);
    }

    @Test
    @DisplayName("쿠폰을 사용하지 않을 수 있다.")
    void noCoupon() {
        Long userId = 1L;
        Long userCouponId = null;

        // when
        QuestionPaymentCoupon questionPaymentCoupon = questionPaymentCouponReader.getQuestionPaymentCoupon(userCouponId, userId);

        // then
        Assertions.assertThat(questionPaymentCoupon).isNull();
    }
}