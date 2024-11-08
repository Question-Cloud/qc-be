package com.eager.questioncloud.core.domain.feed.library.event;

import com.eager.questioncloud.core.domain.payment.question.model.QuestionPayment;
import java.util.List;
import lombok.Getter;

@Getter
public class AppendUserQuestionAfterPaymentEvent {
    private Long userId;
    private List<Long> questionIds;
    private QuestionPayment questionPayment;

    private AppendUserQuestionAfterPaymentEvent(Long userId, List<Long> questionIds, QuestionPayment questionPayment) {
        this.userId = userId;
        this.questionIds = questionIds;
        this.questionPayment = questionPayment;
    }

    public static AppendUserQuestionAfterPaymentEvent create(Long userId, List<Long> questionIds, QuestionPayment questionPayment) {
        return new AppendUserQuestionAfterPaymentEvent(userId, questionIds, questionPayment);
    }
}
