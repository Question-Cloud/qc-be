package com.eager.questioncloud.application.hub;

import com.eager.questioncloud.common.PagingInformation;
import com.eager.questioncloud.domain.review.MyQuestionReview;
import com.eager.questioncloud.domain.review.QuestionReview;
import com.eager.questioncloud.domain.review.QuestionReviewDetail;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionHubReviewService {
    private final QuestionHubReviewReader questionHubReviewReader;
    private final QuestionHubReviewRegister questionHubReviewRegister;
    private final QuestionHubReviewUpdater questionHubReviewUpdater;
    private final QuestionHubReviewRemover questionHubReviewRemover;
    private final ApplicationEventPublisher applicationEventPublisher;

    public int getTotal(Long questionId) {
        return questionHubReviewReader.getTotal(questionId);
    }

    public List<QuestionReviewDetail> getQuestionReviews(Long questionId, Long userId, PagingInformation pagingInformation) {
        return questionHubReviewReader.getQuestionReviews(questionId, userId, pagingInformation);
    }

    public MyQuestionReview getMyQuestionReview(Long questionId, Long userId) {
        return questionHubReviewReader.getMyQuestionReview(questionId, userId);
    }

    //TODO Event 처리
    public void register(QuestionReview questionReview) {
        questionHubReviewRegister.register(questionReview);

//        applicationEventPublisher.publishEvent(RegisteredReviewEvent.create(questionReview.getQuestionId(), questionReview.getRate()));
    }

    //TODO Event 처리
    public void modify(Long reviewId, Long userId, String comment, int rate) {
        QuestionReview questionReview = questionHubReviewReader.findByIdAndUserId(reviewId, userId);
        int varianceRate = rate - questionReview.getRate();
        questionHubReviewUpdater.update(questionReview, comment, rate);

//        applicationEventPublisher.publishEvent(ModifiedReviewEvent.create(questionReview.getQuestionId(), varianceRate));
    }

    //TODO Event 처리
    public void delete(Long reviewId, Long userId) {
        QuestionReview questionReview = questionHubReviewReader.findByIdAndUserId(reviewId, userId);
        questionHubReviewRemover.delete(questionReview);

//        applicationEventPublisher.publishEvent(DeletedReviewEvent.create(questionReview.getQuestionId(), questionReview.getRate()));
    }
}
