package com.eager.questioncloud.core.domain.library.event;

import com.eager.questioncloud.core.domain.payment.model.QuestionPayment;
import com.eager.questioncloud.core.domain.question.model.Question;
import java.util.List;
import lombok.Getter;

@Getter
public class AppendUserQuestionAfterPaymentEvent {
    private Long userId;
    private List<Question> questions;
    private QuestionPayment questionPayment;

    private AppendUserQuestionAfterPaymentEvent(Long userId, List<Question> questions, QuestionPayment questionPayment) {
        this.userId = userId;
        this.questions = questions;
        this.questionPayment = questionPayment;
    }

    public static AppendUserQuestionAfterPaymentEvent create(Long userId, List<Question> questions, QuestionPayment questionPayment) {
        return new AppendUserQuestionAfterPaymentEvent(userId, questions, questionPayment);
    }
}
