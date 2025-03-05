package com.eager.questioncloud.application.business.payment.point.implement;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import com.eager.questioncloud.application.message.MessageSender;
import com.eager.questioncloud.application.utils.Fixture;
import com.eager.questioncloud.core.domain.point.enums.ChargePointPaymentStatus;
import com.eager.questioncloud.core.domain.point.enums.ChargePointType;
import com.eager.questioncloud.core.domain.point.infrastructure.repository.ChargePointPaymentRepository;
import com.eager.questioncloud.core.domain.point.infrastructure.repository.UserPointRepository;
import com.eager.questioncloud.core.domain.point.model.ChargePointPayment;
import com.eager.questioncloud.core.domain.point.model.UserPoint;
import com.eager.questioncloud.core.domain.user.infrastructure.repository.UserRepository;
import com.eager.questioncloud.core.domain.user.model.User;
import com.eager.questioncloud.core.exception.CoreException;
import com.eager.questioncloud.core.exception.Error;
import com.eager.questioncloud.pg.dto.PGPayment;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class ChargePointPaymentApproverTest {
    @Autowired
    private ChargePointPaymentApprover chargePointPaymentApprover;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserPointRepository userPointRepository;

    @SpyBean
    @Autowired
    private MessageSender messageSender;

    @SpyBean
    @Autowired
    private ChargePointPaymentRepository chargePointPaymentRepository;

    @AfterEach
    void tearDown() {
        userRepository.deleteAllInBatch();
        userPointRepository.deleteAllInBatch();
        chargePointPaymentRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("포인트 충전 결제 승인을 할 수 있다.")
    void approve() {
        //given
        User user = userRepository.save(
            Fixture.fixtureMonkey.giveMeBuilder(User.class)
                .set("uid", null)
                .sample()
        );
        userPointRepository.save(new UserPoint(user.getUid(), 0));

        String paymentId = RandomStringUtils.randomAlphanumeric(10);
        ChargePointPayment order = chargePointPaymentRepository.save(ChargePointPayment.order(paymentId, user.getUid(), ChargePointType.PackageA));

        PGPayment pgPayment = new PGPayment(order.getPaymentId(), ChargePointType.PackageA.getAmount(), "https://www.naver.com");

        //when
        chargePointPaymentApprover.approve(pgPayment);

        //then
        ChargePointPayment chargePointPayment = chargePointPaymentRepository.findByPaymentId(paymentId);
        assertThat(chargePointPayment.getChargePointPaymentStatus()).isEqualTo(ChargePointPaymentStatus.PAID);
    }


    @Test
    @DisplayName("이미 처리 된 결제를 승인 요청할 경우 예외가 발생한다.")
    void throwExceptionWhenAlreadyApproved() {
        //given
        User user = userRepository.save(
            Fixture.fixtureMonkey.giveMeBuilder(User.class)
                .set("uid", null)
                .sample()
        );
        userPointRepository.save(new UserPoint(user.getUid(), 0));

        String paymentId = RandomStringUtils.randomAlphanumeric(10);
        ChargePointPayment order = ChargePointPayment.order(paymentId, user.getUid(), ChargePointType.PackageA);
        order.approve("https://www.naver.com");
        chargePointPaymentRepository.save(order);

        PGPayment pgPayment = new PGPayment(order.getPaymentId(), ChargePointType.PackageA.getAmount(), "https://www.naver.com");

        //when then
        assertThatThrownBy(() -> chargePointPaymentApprover.approve(pgPayment))
            .isInstanceOf(CoreException.class)
            .hasFieldOrPropertyWithValue("error", Error.ALREADY_PROCESSED_PAYMENT);
    }

    @Test
    @DisplayName("결제 승인 시 예외가 발생하면 RabbitMQ 결제 실패 메시지를 전송한다.")
    void sendFailChargePointPaymentMessageWhenThrownUnknownException() {
        //given
        User user = userRepository.save(
            Fixture.fixtureMonkey.giveMeBuilder(User.class)
                .set("uid", null)
                .sample()
        );
        userPointRepository.save(new UserPoint(user.getUid(), 0));

        String paymentId = RandomStringUtils.randomAlphanumeric(10);
        ChargePointPayment order = ChargePointPayment.order(paymentId, user.getUid(), ChargePointType.PackageA);
        order.approve("https://www.naver.com");
        chargePointPaymentRepository.save(order);

        PGPayment pgPayment = new PGPayment(order.getPaymentId(), ChargePointType.PackageA.getAmount(), "https://www.naver.com");

        doThrow(new RuntimeException()).when(chargePointPaymentRepository).findByPaymentIdWithLock(any());

        //when then
        assertThatThrownBy(() -> chargePointPaymentApprover.approve(pgPayment))
            .isInstanceOf(RuntimeException.class);

        verify(messageSender).sendMessage(any(), any());
    }

    @Test
    @DisplayName("결제 승인 요청 동시성 이슈를 방지할 수 있다.")
    void preventApproveConcurrency() throws InterruptedException {
        //given
        User user = userRepository.save(
            Fixture.fixtureMonkey.giveMeBuilder(User.class)
                .set("uid", null)
                .sample()
        );
        userPointRepository.save(new UserPoint(user.getUid(), 0));

        String paymentId = RandomStringUtils.randomAlphanumeric(10);
        ChargePointPayment order = chargePointPaymentRepository.save(ChargePointPayment.order(paymentId, user.getUid(), ChargePointType.PackageA));

        PGPayment pgPayment = new PGPayment(order.getPaymentId(), ChargePointType.PackageA.getAmount(), "https://www.naver.com");

        //when
        int numberOfThreads = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failureCount = new AtomicInteger(0);

        //when
        for (int i = 0; i < numberOfThreads; i++) {
            executorService.execute(() -> {
                try {
                    chargePointPaymentApprover.approve(pgPayment);
                    successCount.incrementAndGet();
                } catch (CoreException e) {
                    if (e.getError().equals(Error.ALREADY_PROCESSED_PAYMENT)) {
                        failureCount.incrementAndGet();
                    }
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executorService.shutdown();

        //then
        assertThat(failureCount.get()).isEqualTo(99);
        assertThat(successCount.get()).isEqualTo(1);
    }
}