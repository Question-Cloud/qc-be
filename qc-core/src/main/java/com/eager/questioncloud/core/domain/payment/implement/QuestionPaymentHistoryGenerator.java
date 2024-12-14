package com.eager.questioncloud.core.domain.payment.implement;

import com.eager.questioncloud.core.domain.coupon.infrastructure.CouponRepository;
import com.eager.questioncloud.core.domain.coupon.infrastructure.UserCouponRepository;
import com.eager.questioncloud.core.domain.coupon.model.Coupon;
import com.eager.questioncloud.core.domain.coupon.model.UserCoupon;
import com.eager.questioncloud.core.domain.payment.infrastructure.QuestionPaymentHistoryRepository;
import com.eager.questioncloud.core.domain.payment.model.QuestionPayment;
import com.eager.questioncloud.core.domain.payment.model.QuestionPaymentHistory;
import com.eager.questioncloud.core.domain.question.dto.QuestionInformation;
import com.eager.questioncloud.core.domain.question.infrastructure.QuestionRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionPaymentHistoryGenerator {
    private final QuestionRepository questionRepository;
    private final CouponRepository couponRepository;
    private final UserCouponRepository userCouponRepository;
    private final QuestionPaymentHistoryRepository questionPaymentHistoryRepository;

    public void saveQuestionPaymentHistory(QuestionPayment questionPayment, List<Long> questionIds) {
        List<QuestionInformation> questions = questionRepository.findByQuestionIdIn(questionIds);

        if (questionPayment.isUsingCoupon()) {
            UserCoupon userCoupon = userCouponRepository.getUserCoupon(questionPayment.getUserCouponId());
            Coupon coupon = couponRepository.findById(userCoupon.getCouponId());
            questionPaymentHistoryRepository.save(QuestionPaymentHistory.create(questionPayment, questions, coupon));
        }

        questionPaymentHistoryRepository.save(QuestionPaymentHistory.create(questionPayment, questions, null));
    }
}
