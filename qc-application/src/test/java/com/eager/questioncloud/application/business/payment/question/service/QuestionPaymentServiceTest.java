package com.eager.questioncloud.application.business.payment.question.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.eager.questioncloud.application.business.payment.question.event.QuestionPaymentEvent;
import com.eager.questioncloud.application.utils.Fixture;
import com.eager.questioncloud.core.domain.coupon.infrastructure.repository.CouponRepository;
import com.eager.questioncloud.core.domain.coupon.infrastructure.repository.UserCouponRepository;
import com.eager.questioncloud.core.domain.payment.enums.QuestionPaymentStatus;
import com.eager.questioncloud.core.domain.payment.infrastructure.repository.QuestionOrderRepository;
import com.eager.questioncloud.core.domain.payment.model.QuestionPayment;
import com.eager.questioncloud.core.domain.point.infrastructure.repository.UserPointRepository;
import com.eager.questioncloud.core.domain.point.model.UserPoint;
import com.eager.questioncloud.core.domain.question.enums.QuestionStatus;
import com.eager.questioncloud.core.domain.question.infrastructure.repository.QuestionRepository;
import com.eager.questioncloud.core.domain.question.model.Question;
import com.eager.questioncloud.core.domain.user.infrastructure.repository.UserRepository;
import com.eager.questioncloud.core.domain.user.model.User;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;

@SpringBootTest
@RecordApplicationEvents
@ActiveProfiles("test")
class QuestionPaymentServiceTest {
    @Autowired
    QuestionPaymentService questionPaymentService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private UserCouponRepository userCouponRepository;

    @Autowired
    private UserPointRepository userPointRepository;

    @Autowired
    private QuestionOrderRepository questionOrderRepository;

    @Autowired
    private ApplicationEvents events;

    @AfterEach
    void tearDown() {
        userRepository.deleteAllInBatch();
        questionRepository.deleteAllInBatch();
        couponRepository.deleteAllInBatch();
        userCouponRepository.deleteAllInBatch();
        questionOrderRepository.deleteAllInBatch();
        userPointRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("문제 결제를 할 수 있다.")
    void payment() {
        // given
        User user = userRepository.save(
            Fixture.fixtureMonkey.giveMeBuilder(User.class)
                .set("uid", null)
                .sample()
        );

        userPointRepository.save(new UserPoint(user.getUid(), 1000000));

        List<Long> questionIds = Fixture.fixtureMonkey.giveMeBuilder(Question.class)
            .set("id", null)
            .set("creatorId", 1L)
            .set("questionContent.questionCategoryId", 25L)
            .set("questionContent.price", 1000)
            .set("questionStatus", QuestionStatus.Available)
            .sampleList(10)
            .stream()
            .map(question -> questionRepository.save(question).getId())
            .toList();

        // when
        QuestionPayment questionPayment = questionPaymentService.payment(user.getUid(), questionIds, null);

        // then
        Assertions.assertThat(questionPayment.getStatus()).isEqualTo(QuestionPaymentStatus.SUCCESS);
        Assertions.assertThat(questionPayment.getOrder().getQuestionIds().size()).isEqualTo(questionIds.size());

        long eventCount = events.stream(QuestionPaymentEvent.class).count();
        assertThat(eventCount).isEqualTo(1);
    }
}