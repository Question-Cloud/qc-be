package com.eager.questioncloud.application.api.creator;

import com.eager.questioncloud.core.domain.creator.CreatorStatistics;
import com.eager.questioncloud.core.domain.creator.CreatorStatisticsRepository;
import com.eager.questioncloud.core.domain.payment.CompletedQuestionPaymentEvent;
import com.eager.questioncloud.core.domain.question.Question;
import com.eager.questioncloud.core.domain.question.QuestionRepository;
import com.eager.questioncloud.core.domain.review.DeletedReviewEvent;
import com.eager.questioncloud.core.domain.review.ModifiedReviewEvent;
import com.eager.questioncloud.core.domain.review.RegisteredReviewEvent;
import com.eager.questioncloud.core.domain.subscribe.SubscribedEvent;
import com.eager.questioncloud.core.domain.subscribe.UnsubscribedEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreatorStatisticsProcessor {
    private final CreatorStatisticsRepository creatorStatisticsRepository;
    private final QuestionRepository questionRepository;

    @EventListener
    public void appendCreatorStatistics(RegisteredCreatorEvent event) {
        creatorStatisticsRepository.save(CreatorStatistics.create(event.getCreator().getId()));
    }

    @EventListener
    public void updateCreatorReviewStatistics(RegisteredReviewEvent event) {
        Question question = questionRepository.get(event.getQuestionId());
        CreatorStatistics creatorStatistics = creatorStatisticsRepository.findByCreatorId(question.getCreatorId());
        creatorStatistics.updateReviewStatisticsByRegisteredReview(event.getRate());
        creatorStatisticsRepository.save(creatorStatistics);
    }

    @EventListener
    public void updateCreatorReviewStatistics(ModifiedReviewEvent event) {
        Question question = questionRepository.get(event.getQuestionId());
        CreatorStatistics creatorStatistics = creatorStatisticsRepository.findByCreatorId(question.getCreatorId());
        creatorStatistics.updateReviewStatisticsByModifiedReview(event.getVarianceRate());
        creatorStatisticsRepository.save(creatorStatistics);
    }

    @EventListener
    public void updateCreatorReviewStatistics(DeletedReviewEvent event) {
        Question question = questionRepository.get(event.getQuestionId());
        CreatorStatistics creatorStatistics = creatorStatisticsRepository.findByCreatorId(question.getCreatorId());
        creatorStatistics.updateReviewStatisticsByDeletedReview(event.getRate());
        creatorStatisticsRepository.save(creatorStatistics);
    }

    //TODO 문제 결제는 실패인데 이 이벤트만 성공하면 어떻게 처리해야할지 고민해보기
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
