package com.eager.questioncloud.core.domain.hub.review.implement;

import com.eager.questioncloud.core.domain.hub.review.dto.QuestionReviewUpdateResult;
import com.eager.questioncloud.core.domain.hub.review.model.QuestionReview;
import com.eager.questioncloud.core.domain.hub.review.repository.QuestionReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionReviewRemover {
    private final QuestionReviewRepository questionReviewRepository;

    public QuestionReviewUpdateResult delete(Long reviewId, Long userId) {
        QuestionReview review = questionReviewRepository.findByIdAndUserId(reviewId, userId);
        review.delete();
        questionReviewRepository.save(review);
        return new QuestionReviewUpdateResult(review.getQuestionId(), review.getRate());
    }
}
