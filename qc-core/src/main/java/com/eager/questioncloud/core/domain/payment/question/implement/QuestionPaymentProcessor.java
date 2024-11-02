package com.eager.questioncloud.core.domain.payment.question.implement;

import com.eager.questioncloud.core.domain.hub.question.implement.QuestionReader;
import com.eager.questioncloud.core.domain.hub.question.model.Question;
import com.eager.questioncloud.core.domain.payment.coupon.implement.UserCouponProcessor;
import com.eager.questioncloud.core.domain.payment.coupon.model.Coupon;
import com.eager.questioncloud.core.domain.payment.point.implement.UserPointManager;
import com.eager.questioncloud.core.domain.payment.question.model.QuestionPayment;
import com.eager.questioncloud.core.domain.payment.question.model.QuestionPaymentOrder;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class QuestionPaymentProcessor {
    private final QuestionPaymentAppender questionPaymentAppender;
    private final QuestionPaymentOrderAppender questionPaymentOrderAppender;
    private final QuestionReader questionReader;
    private final UserPointManager userPointManager;
    private final UserCouponProcessor userCouponProcessor;

    @Transactional
    public QuestionPayment questionPayment(Long userId, List<Long> questionIds, Long userCouponId) {
        List<Question> questions = questionReader.getQuestions(questionIds);

        QuestionPayment questionPayment = QuestionPayment.create(userId, userCouponId, questions);

        if (questionPayment.isUsingCoupon()) {
            Coupon coupon = userCouponProcessor.useCoupon(userId, userCouponId);
            questionPayment.useCoupon(coupon);
        }

        userPointManager.usePoint(userId, questionPayment.getAmount());

        questionPayment = questionPaymentAppender.append(questionPayment);
        questionPaymentOrderAppender.createQuestionPaymentOrders(userId, QuestionPaymentOrder.createOrders(questionPayment.getId(), questions));
        return questionPayment;
    }
}
