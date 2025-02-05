package com.eager.questioncloud.application.business.creator.implement;

import com.eager.questioncloud.application.api.payment.question.event.QuestionPaymentEvent;
import com.eager.questioncloud.core.domain.creator.infrastructure.repository.CreatorStatisticsRepository;
import com.eager.questioncloud.core.domain.question.infrastructure.repository.QuestionRepository;
import com.eager.questioncloud.core.domain.question.model.Question;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreatorStatisticsUpdater {
    private final QuestionRepository questionRepository;
    private final CreatorStatisticsRepository creatorStatisticsRepository;

    @EventListener
    public void updateCreatorStatistics(QuestionPaymentEvent event) {
        List<Question> questions = questionRepository.getQuestionsByQuestionIds(event.getQuestionPayment().getOrder().getQuestionIds());
        Map<Long, Long> countQuestionByCreator = questions
            .stream()
            .collect(Collectors.groupingBy(Question::getCreatorId, Collectors.counting()));

        countQuestionByCreator
            .forEach((creatorId, count) -> creatorStatisticsRepository.addSalesCount(creatorId, count.intValue()));
    }
}
