package com.eager.questioncloud.application.api.hub;

import com.eager.questioncloud.core.common.PagingInformation;
import com.eager.questioncloud.core.domain.review.DeletedReviewEvent;
import com.eager.questioncloud.core.domain.review.ModifiedReviewEvent;
import com.eager.questioncloud.core.domain.review.MyQuestionReview;
import com.eager.questioncloud.core.domain.review.QuestionReview;
import com.eager.questioncloud.core.domain.review.QuestionReviewDetail;
import com.eager.questioncloud.core.domain.review.QuestionReviewRepository;
import com.eager.questioncloud.core.domain.review.RegisteredReviewEvent;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionHubReviewService {
    private final QuestionReviewRepository questionReviewRepository;
    private final QuestionHubReviewRegister questionHubReviewRegister;
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
        questionHubReviewRegister.register(questionReview);
        applicationEventPublisher.publishEvent(RegisteredReviewEvent.create(questionReview.getQuestionId(), questionReview.getRate()));
    }

    public void modify(Long reviewId, Long userId, String comment, int rate) {
        QuestionReview questionReview = questionReviewRepository.findByIdAndUserId(reviewId, userId);
        int varianceRate = rate - questionReview.getRate();
        questionReview.modify(comment, rate);
        questionReviewRepository.save(questionReview);
        applicationEventPublisher.publishEvent(ModifiedReviewEvent.create(questionReview.getQuestionId(), varianceRate));
    }
    
    public void delete(Long reviewId, Long userId) {
        QuestionReview questionReview = questionReviewRepository.findByIdAndUserId(reviewId, userId);
        questionReview.delete();
        questionReviewRepository.save(questionReview);
        applicationEventPublisher.publishEvent(DeletedReviewEvent.create(questionReview.getQuestionId(), questionReview.getRate()));
    }
}