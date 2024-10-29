package com.eager.questioncloud.core.domain.questionhub.payment.implement;

import com.eager.questioncloud.core.domain.questionhub.payment.model.Coupon;
import com.eager.questioncloud.core.domain.questionhub.payment.model.QuestionPayment;
import com.eager.questioncloud.core.domain.questionhub.payment.model.QuestionPaymentOrder;
import com.eager.questioncloud.core.domain.questionhub.question.implement.QuestionReader;
import com.eager.questioncloud.core.domain.questionhub.question.model.Question;
import com.eager.questioncloud.core.domain.user.coupon.implement.UserCouponProcessor;
import com.eager.questioncloud.core.domain.user.point.implement.UserPointProcessor;
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
