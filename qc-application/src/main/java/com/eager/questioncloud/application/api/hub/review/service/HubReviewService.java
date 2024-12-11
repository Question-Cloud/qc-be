package com.eager.questioncloud.application.api.hub.review.service;

import com.eager.questioncloud.application.api.hub.review.implement.HubReviewRegister;
import com.eager.questioncloud.application.api.hub.review.implement.HunReviewUpdater;
import com.eager.questioncloud.core.common.PagingInformation;
import com.eager.questioncloud.core.domain.review.dto.MyQuestionReview;
import com.eager.questioncloud.core.domain.review.dto.QuestionReviewDetail;
import com.eager.questioncloud.core.domain.review.event.DeletedReviewEvent;
import com.eager.questioncloud.core.domain.review.event.ModifiedReviewEvent;
import com.eager.questioncloud.core.domain.review.event.RegisteredReviewEvent;
import com.eager.questioncloud.core.domain.review.infrastructure.QuestionReviewRepository;
import com.eager.questioncloud.core.domain.review.model.QuestionReview;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HubReviewService {
    private final QuestionReviewRepository questionReviewRepository;
    private final HubReviewRegister hubReviewRegister;
    private final HunReviewUpdater questionHubReviewUpdater;
    private final ApplicationEventPublisher applicationEventPublisher;

    public int getTotal(Long questionId) {
        return questionReviewRepository.getTotal(questionId);
    }

    public List<QuestionReviewDetail> getQuestionReviews(Long questionId, Long userId, PagingInformation pagingInformation) {
        return questionReviewRepository.getQuestionReviews(questionId, userId, pagingInformation);
    }

    public MyQuestionReview getMyQuestionReview(Long questionId, Long userId) {
        QuestionReview questionReview = questionReviewRepository.getMyQuestionReview(questionId, userId);
        return MyQuestionReview.from(questionReview);
    }

    public void register(QuestionReview questionReview) {
        hubReviewRegister.register(questionReview);
        applicationEventPublisher.publishEvent(RegisteredReviewEvent.create(questionReview.getQuestionId(), questionReview.getRate()));
    }

    public void modify(Long reviewId, Long userId, String comment, int rate) {
        QuestionReview questionReview = questionReviewRepository.findByIdAndUserId(reviewId, userId);
        int varianceRate = questionHubReviewUpdater.modifyQuestionReview(questionReview, comment, rate);
        applicationEventPublisher.publishEvent(ModifiedReviewEvent.create(questionReview.getQuestionId(), varianceRate));
    }

    public void delete(Long reviewId, Long userId) {
        QuestionReview questionReview = questionReviewRepository.findByIdAndUserId(reviewId, userId);
        questionReview.delete();
        questionReviewRepository.save(questionReview);
        applicationEventPublisher.publishEvent(DeletedReviewEvent.create(questionReview.getQuestionId(), questionReview.getRate()));
    }
}
