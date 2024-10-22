package com.eager.questioncloud.domain.payment.implement;

import com.eager.questioncloud.domain.coupon.implement.UserCouponProcessor;
import com.eager.questioncloud.domain.coupon.model.Coupon;
import com.eager.questioncloud.domain.payment.model.QuestionPayment;
import com.eager.questioncloud.domain.point.implement.UserPointProcessor;
import com.eager.questioncloud.domain.question.implement.QuestionReader;
import com.eager.questioncloud.domain.question.model.Question;
import com.eager.questioncloud.domain.payment.model.QuestionPaymentOrder;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionPaymentProcessor {
    private final QuestionPaymentAppender questionPaymentAppender;
    private final QuestionPaymentOrderAppender questionPaymentOrderAppender;
    private final QuestionReader questionReader;
    private final UserPointProcessor userPointProcessor;
    private final UserCouponProcessor userCouponProcessor;

    @Transactional
    public QuestionPayment questionPayment(Long userId, List<Long> questionIds, Long userCouponId) {
        List<Question> questions = questionReader.getQuestions(questionIds);

        QuestionPayment questionPayment = QuestionPayment.create(userId, userCouponId, questions);

        if (questionPayment.isUsingCoupon()) {
            Coupon coupon = userCouponProcessor.useCoupon(userId, userCouponId);
            questionPayment.useCoupon(coupon);
        }

        userPointProcessor.usePoint(userId, questionPayment.getAmount());

        questionPayment = questionPaymentAppender.append(questionPayment);
        questionPaymentOrderAppender.createQuestionPaymentOrders(QuestionPaymentOrder.createOrders(questionPayment.getId(), questions));
        return questionPayment;
    }
}
