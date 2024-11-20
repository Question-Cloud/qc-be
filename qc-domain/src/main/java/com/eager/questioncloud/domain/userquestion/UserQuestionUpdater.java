package com.eager.questioncloud.domain.userquestion;

import com.eager.questioncloud.domain.payment.CompletedQuestionPaymentEvent;
import com.eager.questioncloud.exception.FailQuestionPaymentException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserQuestionUpdater {
    private final UserQuestionRepository userQuestionRepository;

    @EventListener
    public void appendUserQuestionAfterPayment(CompletedQuestionPaymentEvent event) {
        try {
            if (true) {
                throw new RuntimeException("");
            }
            userQuestionRepository.saveAll(UserQuestion.create(event.getQuestionPayment().getUserId(), event.getQuestionIds()));
        } catch (Exception e) {
            throw new FailQuestionPaymentException(event.getQuestionPayment().getPaymentId());
        }
    }
}
