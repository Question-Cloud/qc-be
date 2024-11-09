package com.eager.questioncloud.core.domain.hub.review.service;

import com.eager.questioncloud.core.common.PagingInformation;
import com.eager.questioncloud.core.domain.hub.review.dto.QuestionReviewDto.QuestionReviewItem;
import com.eager.questioncloud.core.domain.hub.review.dto.QuestionReviewUpdateResult;
import com.eager.questioncloud.core.domain.hub.review.event.UpdateReviewStatisticsEvent;
import com.eager.questioncloud.core.domain.hub.review.event.UpdateReviewType;
import com.eager.questioncloud.core.domain.hub.review.implement.QuestionReviewAppender;
import com.eager.questioncloud.core.domain.hub.review.implement.QuestionReviewReader;
import com.eager.questioncloud.core.domain.hub.review.implement.QuestionReviewRemover;
import com.eager.questioncloud.core.domain.hub.review.implement.QuestionReviewUpdater;
import com.eager.questioncloud.core.domain.hub.review.model.QuestionReview;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionReviewService {
    private final QuestionReviewReader questionReviewReader;
    private final QuestionReviewAppender questionReviewAppender;
    private final QuestionReviewUpdater questionReviewUpdater;
    private final QuestionReviewRemover questionReviewRemover;
    private final ApplicationEventPublisher applicationEventPublisher;

    public int getTotal(Long questionId) {
        return questionReviewReader.getTotal(questionId);
    }

    public List<QuestionReviewItem> getQuestionReviews(Long questionId, Long userId, PagingInformation pagingInformation) {
        return questionReviewReader.getQuestionReviews(questionId, userId, pagingInformation);
    }

    public QuestionReview getMyQuestionReview(Long questionId, Long userId) {
        return questionReviewReader.getMyQuestionReview(questionId, userId);
    }

    public QuestionReview register(QuestionReview questionReview) {
        questionReviewAppender.append(questionReview);
        applicationEventPublisher.publishEvent(
            UpdateReviewStatisticsEvent.create(questionReview.getQuestionId(), questionReview.getRate(), UpdateReviewType.REGISTER)
        );
        return questionReview;
    }

    public void modify(Long reviewId, Long userId, String comment, int rate) {
        QuestionReviewUpdateResult questionReviewUpdateResult = questionReviewUpdater.update(reviewId, userId, comment, rate);
        applicationEventPublisher.publishEvent(
            UpdateReviewStatisticsEvent.create(
                questionReviewUpdateResult.getQuestionId(),
                questionReviewUpdateResult.getVarianceRate(),
                UpdateReviewType.MODIFY)
        );
    }

    public void delete(Long reviewId, Long userId) {
        questionReviewRemover.delete(reviewId, userId);
    }
}
