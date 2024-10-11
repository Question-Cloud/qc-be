package com.eager.questioncloud.review.implement;

import com.eager.questioncloud.review.domain.QuestionReview;
import com.eager.questioncloud.review.repository.QuestionReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class QuestionReviewAppender {
    private final QuestionReviewRepository questionReviewRepository;
    private final QuestionReviewValidator questionReviewValidator;
    private final QuestionReviewStatisticsUpdater questionReviewStatisticsUpdater;

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public QuestionReview append(QuestionReview questionReview) {
        questionReviewValidator.validate(questionReview);
        questionReviewRepository.save(questionReview);
        questionReviewStatisticsUpdater.updateByNewReview(questionReview.getQuestionId(), questionReview.getRate());
        return questionReview;
    }
}
