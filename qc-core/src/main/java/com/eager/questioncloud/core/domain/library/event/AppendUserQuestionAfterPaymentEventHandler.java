package com.eager.questioncloud.core.domain.library.event;

import com.eager.questioncloud.core.domain.library.implement.UserQuestionAppender;
import com.eager.questioncloud.core.domain.payment.implement.QuestionPaymentFailHandler;
import com.eager.questioncloud.core.exception.CustomException;
import com.eager.questioncloud.core.exception.Error;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AppendUserQuestionAfterPaymentEventHandler {
    private final UserQuestionAppender userQuestionAppender;
    private final QuestionPaymentFailHandler questionPaymentFailHandler;

    @EventListener
    public void appendUserQuestionAfterPayment(AppendUserQuestionAfterPaymentEvent appendUserQuestionAfterPaymentEvent) {
        try {
            userQuestionAppender.appendUserQuestions(
                appendUserQuestionAfterPaymentEvent.getUserId(),
                appendUserQuestionAfterPaymentEvent.getQuestions());
        } catch (Exception e) {
            questionPaymentFailHandler.failHandler(appendUserQuestionAfterPaymentEvent.getQuestionPayment());
            throw new CustomException(Error.INTERNAL_SERVER_ERROR);
        }
    }
}
