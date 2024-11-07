package com.eager.questioncloud.core.domain.feed.library.event;

import com.eager.questioncloud.core.domain.payment.question.model.QuestionPayment;
import java.util.List;
import lombok.Getter;

@Getter
public class AppendUserQuestionEvent {
    private Long userId;
    private List<Long> questionIds;
    private QuestionPayment questionPayment;

    private AppendUserQuestionEvent(Long userId, List<Long> questionIds, QuestionPayment questionPayment) {
        this.userId = userId;
        this.questionIds = questionIds;
        this.questionPayment = questionPayment;
    }

    public static AppendUserQuestionEvent create(Long userId, List<Long> questionIds, QuestionPayment questionPayment) {
        return new AppendUserQuestionEvent(userId, questionIds, questionPayment);
    }
}
