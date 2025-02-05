package com.eager.questioncloud.application.business.payment.point.implement;

import com.eager.questioncloud.application.business.payment.point.event.ChargePointPaymentEvent;
import com.eager.questioncloud.application.utils.Fixture;
import com.eager.questioncloud.core.domain.point.enums.ChargePointType;
import com.eager.questioncloud.core.domain.point.infrastructure.repository.ChargePointPaymentRepository;
import com.eager.questioncloud.core.domain.point.infrastructure.repository.UserPointRepository;
import com.eager.questioncloud.core.domain.point.model.ChargePointPayment;
import com.eager.questioncloud.core.domain.point.model.UserPoint;
import com.eager.questioncloud.core.domain.user.infrastructure.repository.UserRepository;
import com.eager.questioncloud.core.domain.user.model.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class ChargePointPaymentPostProcessorTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserPointRepository userPointRepository;

    @Autowired
    private ChargePointPaymentRepository chargePointPaymentRepository;

    @SpyBean
    @Autowired
    private ChargePointPaymentPostProcessor chargePointPaymentPostProcessor;

    @AfterEach
    void tearDown() {
        userRepository.deleteAllInBatch();
        userPointRepository.deleteAllInBatch();
        chargePointPaymentRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("포인트 충전 결제 완료 시 포인트 충전 이벤트를 처리할 수 있다.")
    void chargeUserPointWhenReceivedChargePointPaymentEvent() {
        // given
        User user = userRepository.save(
            Fixture.fixtureMonkey.giveMeBuilder(User.class)
                .set("uid", null)
                .sample()
        );
        userPointRepository.save(new UserPoint(user.getUid(), 0));

        String paymentId = RandomStringUtils.randomAlphanumeric(10);
        ChargePointPayment payment = chargePointPaymentRepository.save(ChargePointPayment.order(paymentId, user.getUid(), ChargePointType.PackageA));
        payment.approve("approve");
        chargePointPaymentRepository.save(payment);

        // when
        chargePointPaymentPostProcessor.chargeUserPoint(ChargePointPaymentEvent.from(payment));

        //then
        UserPoint userPoint = userPointRepository.getUserPoint(user.getUid());
        Assertions.assertThat(userPoint.getPoint()).isEqualTo(ChargePointType.PackageA.getAmount());
    }
}