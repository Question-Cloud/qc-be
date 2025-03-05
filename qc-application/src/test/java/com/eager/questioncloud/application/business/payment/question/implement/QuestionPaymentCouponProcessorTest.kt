package com.eager.questioncloud.application.business.payment.question.implement;

import com.eager.questioncloud.application.utils.Fixture;
import com.eager.questioncloud.core.domain.coupon.enums.CouponType;
import com.eager.questioncloud.core.domain.coupon.infrastructure.repository.CouponRepository;
import com.eager.questioncloud.core.domain.coupon.infrastructure.repository.UserCouponRepository;
import com.eager.questioncloud.core.domain.coupon.model.Coupon;
import com.eager.questioncloud.core.domain.coupon.model.UserCoupon;
import com.eager.questioncloud.core.domain.payment.model.QuestionOrder;
import com.eager.questioncloud.core.domain.payment.model.QuestionOrderItem;
import com.eager.questioncloud.core.domain.payment.model.QuestionPayment;
import com.eager.questioncloud.core.domain.payment.model.QuestionPaymentCoupon;
import com.eager.questioncloud.core.domain.user.infrastructure.repository.UserRepository;
import com.eager.questioncloud.core.domain.user.model.User;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class QuestionPaymentCouponProcessorTest {
    @Autowired
    UserCouponRepository userCouponRepository;

    @Autowired
    CouponRepository couponRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private QuestionPaymentCouponProcessor questionPaymentCouponProcessor;

    @AfterEach
    void tearDown() {
        userCouponRepository.deleteAllInBatch();
        couponRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("쿠폰을 적용하여 금액을 할인할 수 있다.")
    void applyCoupon() {
        // given
        User user = userRepository.save(
            Fixture.fixtureMonkey.giveMeBuilder(User.class)
                .set("uid", null)
                .sample()
        );

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

        QuestionPaymentCoupon questionPaymentCoupon = QuestionPaymentCoupon.create(userCoupon.getId(), coupon);

        List<QuestionOrderItem> orderItems = Fixture.fixtureMonkey.giveMeBuilder(QuestionOrderItem.class)
            .set("price", 1000)
            .sampleList(10);

        QuestionOrder questionOrder = new QuestionOrder(UUID.randomUUID().toString(), orderItems);

        QuestionPayment questionPayment = QuestionPayment.create(user.getUid(), questionPaymentCoupon, questionOrder);
        int originalAmount = questionPayment.getAmount();

        // when
        questionPaymentCouponProcessor.applyCoupon(questionPayment);

        // then
        int afterAmount = questionPayment.getAmount();
        int saleAmount = originalAmount - afterAmount;

        Assertions.assertThat(saleAmount).isEqualTo(questionPaymentCoupon.getValue());
    }
}