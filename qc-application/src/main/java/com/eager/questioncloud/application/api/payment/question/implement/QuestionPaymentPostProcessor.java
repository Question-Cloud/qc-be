package com.eager.questioncloud.application.api.payment.question.implement;

import com.eager.questioncloud.application.api.payment.question.event.FailQuestionPaymentEvent;
import com.eager.questioncloud.application.message.MessageSender;
import com.eager.questioncloud.application.message.MessageType;
import com.eager.questioncloud.core.domain.creator.implement.CreatorStatisticsProcessor;
import com.eager.questioncloud.core.domain.payment.implement.QuestionPaymentHistoryGenerator;
import com.eager.questioncloud.core.domain.payment.model.QuestionOrder;
import com.eager.questioncloud.core.domain.payment.model.QuestionPayment;
import com.eager.questioncloud.core.domain.question.infrastructure.repository.QuestionRepository;
import com.eager.questioncloud.core.domain.question.model.Question;
import com.eager.questioncloud.core.domain.userquestion.implement.UserQuestionAppender;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionPaymentPostProcessor {
    private final CreatorStatisticsProcessor creatorStatisticsProcessor;
    private final UserQuestionAppender userQuestionAppender;
    private final QuestionRepository questionRepository;
    private final QuestionPaymentHistoryGenerator questionPaymentHistoryGenerator;
    private final MessageSender messageSender;

    public void postProcess(QuestionPayment questionPayment) {
        try {
            if (true) {
                throw new RuntimeException("");
            }
            updateCreatorStatistics(questionPayment.getOrder());
            updateSalesCount(questionPayment.getOrder());
            userQuestionAppender.appendUserQuestion(questionPayment.getUserId(), questionPayment.getOrder().getQuestionIds());
            questionPaymentHistoryGenerator.saveQuestionPaymentHistory(questionPayment);
        } catch (Exception e) {
            messageSender.sendMessage(
                MessageType.FAIL_QUESTION_PAYMENT,
                new FailQuestionPaymentEvent(questionPayment)
            );
        }
    }

    private void updateCreatorStatistics(QuestionOrder order) {
        List<Question> questions = questionRepository.getQuestionsByQuestionIds(order.getQuestionIds());
        Map<Long, Long> countQuestionByCreator = questions
            .stream()
            .collect(Collectors.groupingBy(Question::getCreatorId, Collectors.counting()));

        countQuestionByCreator
            .forEach((creatorId, count) -> creatorStatisticsProcessor.updateSalesCount(creatorId, count.intValue()));
    }

    private void updateSalesCount(QuestionOrder order) {
        order.getItems()
            .forEach(item -> questionRepository.increaseQuestionCount(item.getQuestionId()));
    }
}
