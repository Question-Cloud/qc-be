package com.eager.questioncloud.core.domain.review.service;

import com.eager.questioncloud.core.common.PagingInformation;
import com.eager.questioncloud.core.domain.review.dto.QuestionReviewItem;
import com.eager.questioncloud.core.domain.review.event.DeletedReviewEvent;
import com.eager.questioncloud.core.domain.review.event.ModifiedReviewEvent;
import com.eager.questioncloud.core.domain.review.event.RegisteredReviewEvent;
import com.eager.questioncloud.core.domain.review.implement.QuestionReviewReader;
import com.eager.questioncloud.core.domain.review.implement.QuestionReviewRegister;
import com.eager.questioncloud.core.domain.review.implement.QuestionReviewRemover;
import com.eager.questioncloud.core.domain.review.implement.QuestionReviewUpdater;
import com.eager.questioncloud.core.domain.review.model.QuestionReview;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionReviewService {
    private final QuestionReviewReader questionReviewReader;
    private final QuestionReviewRegister questionReviewRegister;
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
        questionReviewRegister.register(questionReview);

        applicationEventPublisher.publishEvent(RegisteredReviewEvent.create(questionReview.getQuestionId(), questionReview.getRate()));
        return questionReview;
    }

    public void modify(Long reviewId, Long userId, String comment, int rate) {
        QuestionReview questionReview = questionReviewReader.findByIdAndUserId(reviewId, userId);
        int varianceRate = rate - questionReview.getRate();
        questionReviewUpdater.update(questionReview, comment, rate);

        applicationEventPublisher.publishEvent(ModifiedReviewEvent.create(questionReview.getQuestionId(), varianceRate));
    }

    public void delete(Long reviewId, Long userId) {
        QuestionReview questionReview = questionReviewReader.findByIdAndUserId(reviewId, userId);
        questionReviewRemover.delete(questionReview);

        applicationEventPublisher.publishEvent(DeletedReviewEvent.create(questionReview.getQuestionId(), questionReview.getRate()));
    }
}
