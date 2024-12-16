package com.eager.questioncloud.core.domain.payment.implement;

import com.eager.questioncloud.core.domain.coupon.infrastructure.CouponRepository;
import com.eager.questioncloud.core.domain.coupon.infrastructure.UserCouponRepository;
import com.eager.questioncloud.core.domain.coupon.model.Coupon;
import com.eager.questioncloud.core.domain.coupon.model.UserCoupon;
import com.eager.questioncloud.core.domain.payment.event.CompletedQuestionPaymentEvent;
import com.eager.questioncloud.core.domain.payment.infrastructure.QuestionPaymentHistoryRepository;
import com.eager.questioncloud.core.domain.payment.model.QuestionPaymentHistory;
import com.eager.questioncloud.core.domain.question.dto.QuestionInformation;
import com.eager.questioncloud.core.domain.question.infrastructure.QuestionRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionPaymentHistoryGenerator {
    private final QuestionRepository questionRepository;
    private final CouponRepository couponRepository;
    private final UserCouponRepository userCouponRepository;
    private final QuestionPaymentHistoryRepository questionPaymentHistoryRepository;

    @EventListener
    @Async
    public void saveQuestionPaymentHistory(CompletedQuestionPaymentEvent event) {
        List<QuestionInformation> questions = questionRepository.findByQuestionIdIn(event.getQuestionIds());

        if (event.getQuestionPayment().isUsingCoupon()) {
            UserCoupon userCoupon = userCouponRepository.getUserCoupon(event.getQuestionPayment().getUserCouponId());
            Coupon coupon = couponRepository.findById(userCoupon.getCouponId());
            questionPaymentHistoryRepository.save(QuestionPaymentHistory.create(event.getQuestionPayment(), questions, coupon));
        }

        questionPaymentHistoryRepository.save(QuestionPaymentHistory.create(event.getQuestionPayment(), questions, null));
    }
}
