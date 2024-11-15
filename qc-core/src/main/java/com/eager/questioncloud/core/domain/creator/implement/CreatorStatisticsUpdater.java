package com.eager.questioncloud.core.domain.creator.implement;

import com.eager.questioncloud.core.domain.creator.model.CreatorStatistics;
import com.eager.questioncloud.core.domain.creator.repository.CreatorStatisticsRepository;
import com.eager.questioncloud.core.domain.payment.event.CompletedQuestionPaymentEvent;
import com.eager.questioncloud.core.domain.question.model.Question;
import com.eager.questioncloud.core.domain.question.repository.QuestionRepository;
import com.eager.questioncloud.core.domain.subscribe.event.SubscribedEvent;
import com.eager.questioncloud.core.domain.subscribe.event.UnsubscribedEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreatorStatisticsUpdater {
    private final CreatorStatisticsRepository creatorStatisticsRepository;
    private final QuestionRepository questionRepository;

    @EventListener
    public void updateSalesCount(CompletedQuestionPaymentEvent event) {
        List<Question> questions = questionRepository.getQuestionsByQuestionIds(event.getQuestionIds());
        Map<Long, Long> countQuestionByCreator = questions.stream().collect(Collectors.groupingBy(Question::getCreatorId, Collectors.counting()));
        List<CreatorStatistics> updateStatistics = new ArrayList<>();

        countQuestionByCreator.forEach((creatorId, count) -> {
            CreatorStatistics creatorStatistics = creatorStatisticsRepository.findByCreatorId(creatorId);
            creatorStatistics.addSaleCount(count.intValue());
            updateStatistics.add(creatorStatistics);
        });

        creatorStatisticsRepository.saveAll(updateStatistics);
    }

    @EventListener
    public void increaseSubscribeCount(SubscribedEvent event) {
        CreatorStatistics creatorStatistics = creatorStatisticsRepository.findByCreatorId(event.getSubscribe().getCreatorId());
        creatorStatistics.increaseSubscribeCount();
        creatorStatisticsRepository.save(creatorStatistics);
    }

    @EventListener
    public void decreaseSubscribeCount(UnsubscribedEvent event) {
        CreatorStatistics creatorStatistics = creatorStatisticsRepository.findByCreatorId(event.getCreatorId());
        creatorStatistics.decreaseSubscribeCount();
        creatorStatisticsRepository.save(creatorStatistics);
    }
}
