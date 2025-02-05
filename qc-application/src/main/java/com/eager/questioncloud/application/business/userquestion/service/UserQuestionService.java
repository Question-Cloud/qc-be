package com.eager.questioncloud.application.business.userquestion.service;

import com.eager.questioncloud.application.business.payment.question.event.QuestionPaymentEvent;
import com.eager.questioncloud.core.domain.question.common.QuestionFilter;
import com.eager.questioncloud.core.domain.userquestion.dto.UserQuestionDetail;
import com.eager.questioncloud.core.domain.userquestion.infrastructure.repository.UserQuestionRepository;
import com.eager.questioncloud.core.domain.userquestion.model.UserQuestion;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserQuestionService {
    private final UserQuestionRepository userQuestionRepository;

    public List<UserQuestionDetail> getUserQuestions(QuestionFilter questionFilter) {
        return userQuestionRepository.getUserQuestions(questionFilter);
    }

    public int countUserQuestions(QuestionFilter questionFilter) {
        return userQuestionRepository.countUserQuestions(questionFilter);
    }

    @EventListener
    public void appendUserQuestion(QuestionPaymentEvent questionPaymentEvent) {
        userQuestionRepository.saveAll(
            UserQuestion.create(
                questionPaymentEvent.getQuestionPayment().getUserId(),
                questionPaymentEvent.getQuestionPayment().getOrder().getQuestionIds())
        );
    }
}
