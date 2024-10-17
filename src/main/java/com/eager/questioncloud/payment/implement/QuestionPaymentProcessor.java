package com.eager.questioncloud.payment.implement;

import com.eager.questioncloud.coupon.implement.UserCouponProcessor;
import com.eager.questioncloud.exception.CustomException;
import com.eager.questioncloud.exception.Error;
import com.eager.questioncloud.library.implement.UserQuestionLibraryReader;
import com.eager.questioncloud.payment.model.QuestionPayment;
import com.eager.questioncloud.payment.model.QuestionPaymentOrder;
import com.eager.questioncloud.point.implement.UserPointProcessor;
import com.eager.questioncloud.question.implement.QuestionReader;
import com.eager.questioncloud.question.model.Question;
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
    private final UserCouponProcessor userCouponProcessor;
    private final UserQuestionLibraryReader userQuestionLibraryReader;

    @Transactional
    public QuestionPayment questionPayment(Long userId, List<Long> questionIds, Long userCouponId) {
        if (userQuestionLibraryReader.isOwned(userId, questionIds)) {
            throw new CustomException(Error.ALREADY_OWN_QUESTION);
        }
        
        List<Question> questions = questionReader.getQuestions(questionIds);
        int originalAmount = getOriginalAmount(questions);
        int finalAmount = isUsingCoupon(userCouponId) ? userCouponProcessor.useCoupon(userId, userCouponId, originalAmount) : originalAmount;

        userPointProcessor.usePoint(userId, finalAmount);

        QuestionPayment questionPayment = questionPaymentAppender.createQuestionPayment(QuestionPayment.create(userId, userCouponId, finalAmount));
        questionPaymentOrderAppender.createQuestionPaymentOrders(QuestionPaymentOrder.createOrders(questionPayment.getId(), questions));
        return questionPayment;
    }

    public Boolean isUsingCoupon(Long couponId) {
        return couponId != null;
    }

    public int getOriginalAmount(List<Question> questions) {
        return questions
            .stream()
            .mapToInt(question -> question.getQuestionContent().getPrice())
            .sum();
    }
}
