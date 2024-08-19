package com.eager.questioncloud.payment;

import com.eager.questioncloud.library.UserQuestionLibraryCreator;
import com.eager.questioncloud.question.Question;
import com.eager.questioncloud.question.QuestionReader;
import com.eager.questioncloud.user.UserPointManager;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionPaymentProcessor {
    private final QuestionReader questionReader;
    private final QuestionPaymentCreator questionPaymentCreator;
    private final QuestionPaymentOrderCreator questionPaymentOrderCreator;
    private final UserPointManager userPointManager;
    private final UserQuestionLibraryCreator userQuestionLibraryCreator;

    @Transactional
    public QuestionPayment questionPayment(Long userId, List<Long> questionIds, Long couponId) {
        int originalAmount = getOriginalAmount(questionIds);
        int finalAmount = originalAmount; //Todo add Coupon Logic

        userPointManager.usePoint(userId, finalAmount);

        QuestionPayment questionPayment = questionPaymentCreator.append(QuestionPayment.create(userId, couponId, finalAmount));
        questionPaymentOrderCreator.append(QuestionPaymentOrder.createOrders(questionPayment.getId(), questionIds));

        userQuestionLibraryCreator.appendUserQuestion(userId, questionIds);
        return questionPayment;
    }

    public int getOriginalAmount(List<Long> questionIds) {
        return questionReader.getQuestions(questionIds)
            .stream()
            .mapToInt(Question::getPrice)
            .sum();
    }
}
