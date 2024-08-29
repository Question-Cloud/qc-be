package com.eager.questioncloud.payment;

import com.eager.questioncloud.coupon.UserCouponProcessor;
import com.eager.questioncloud.library.UserQuestionLibraryAppender;
import com.eager.questioncloud.point.UserPointProcessor;
import com.eager.questioncloud.question.Question;
import com.eager.questioncloud.question.QuestionReader;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionPaymentProcessor {
    private final QuestionReader questionReader;
    private final QuestionPaymentAppender questionPaymentAppender;
    private final QuestionPaymentOrderAppender questionPaymentOrderAppender;
    private final UserPointProcessor userPointProcessor;
    private final UserQuestionLibraryAppender userQuestionLibraryAppender;
    private final UserCouponProcessor userCouponProcessor;

    @Transactional
    public QuestionPayment questionPayment(Long userId, List<Long> questionIds, Long userCouponId) {
        int originalAmount = getOriginalAmount(questionIds);
        int finalAmount = isUsingCoupon(userCouponId) ? userCouponProcessor.useCoupon(userId, userCouponId, originalAmount) : originalAmount;

        userPointProcessor.usePoint(userId, finalAmount);

        QuestionPayment questionPayment = questionPaymentAppender.createQuestionPayment(QuestionPayment.create(userId, userCouponId, finalAmount));
        questionPaymentOrderAppender.createQuestionPaymentOrders(QuestionPaymentOrder.createOrders(questionPayment.getId(), questionIds));

        userQuestionLibraryAppender.appendUserQuestion(userId, questionIds);
        return questionPayment;
    }

    public Boolean isUsingCoupon(Long couponId) {
        return couponId != null;
    }

    public int getOriginalAmount(List<Long> questionIds) {
        return questionReader.getQuestions(questionIds)
            .stream()
            .mapToInt(Question::getPrice)
            .sum();
    }
}
