package com.eager.questioncloud.core.domain.library.implement;

import com.eager.questioncloud.core.domain.library.model.UserQuestion;
import com.eager.questioncloud.core.domain.library.repository.UserQuestionRepository;
import com.eager.questioncloud.core.domain.payment.event.CompletedQuestionPaymentEvent;
import com.eager.questioncloud.core.domain.payment.implement.QuestionPaymentFailHandler;
import com.eager.questioncloud.core.exception.CustomException;
import com.eager.questioncloud.core.exception.Error;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserQuestionAppender {
    private final UserQuestionRepository userQuestionRepository;
    private final QuestionPaymentFailHandler questionPaymentFailHandler;

    @EventListener
    public void appendUserQuestionAfterPayment(CompletedQuestionPaymentEvent event) {
        try {
            userQuestionRepository.saveAll(UserQuestion.create(event.getQuestionPayment().getUserId(), event.getQuestionIds()));
        } catch (Exception e) {
            questionPaymentFailHandler.failHandler(event.getQuestionPayment());
            throw new CustomException(Error.INTERNAL_SERVER_ERROR);
        }
    }
}
