package com.eager.questioncloud.application.api.payment.question.implement;

import com.eager.questioncloud.application.api.payment.question.event.QuestionPaymentEvent;
import com.eager.questioncloud.application.message.FailQuestionPaymentMessage;
import com.eager.questioncloud.application.message.MessageSender;
import com.eager.questioncloud.application.message.MessageType;
import com.eager.questioncloud.core.domain.creator.implement.CreatorStatisticsProcessor;
import com.eager.questioncloud.core.domain.question.infrastructure.repository.QuestionRepository;
import com.eager.questioncloud.core.domain.question.model.Question;
import com.eager.questioncloud.core.domain.userquestion.implement.UserQuestionAppender;
import com.eager.questioncloud.core.exception.CoreException;
import com.eager.questioncloud.core.exception.Error;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionPaymentPostProcessor {
    private final CreatorStatisticsProcessor creatorStatisticsProcessor;
    private final UserQuestionAppender userQuestionAppender;
    private final QuestionRepository questionRepository;
    private final MessageSender messageSender;

    @EventListener
    public void updateCreatorStatistics(QuestionPaymentEvent event) {
        List<Question> questions = questionRepository.getQuestionsByQuestionIds(event.getQuestionPayment().getOrder().getQuestionIds());
        Map<Long, Long> countQuestionByCreator = questions
            .stream()
            .collect(Collectors.groupingBy(Question::getCreatorId, Collectors.counting()));

        countQuestionByCreator
            .forEach((creatorId, count) -> creatorStatisticsProcessor.updateSalesCount(creatorId, count.intValue()));
    }

    @EventListener
    public void updateSalesCount(QuestionPaymentEvent event) {
        event.getQuestionPayment().getOrder().getItems()
            .forEach(item -> questionRepository.increaseQuestionCount(item.getQuestionId()));
    }

    @EventListener
    public void appendUserQuestion(QuestionPaymentEvent event) {
        try {
            userQuestionAppender.appendUserQuestion(event.getQuestionPayment().getUserId(), event.getQuestionPayment().getOrder().getQuestionIds());
        } catch (Exception e) {
            messageSender.sendMessage(MessageType.FAIL_QUESTION_PAYMENT, new FailQuestionPaymentMessage(event.getQuestionPayment()));
            throw new CoreException(Error.PAYMENT_ERROR);
        }
    }
}
