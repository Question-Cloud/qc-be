package com.eager.questioncloud.application.hub;

import com.eager.questioncloud.domain.review.QuestionReview;
import com.eager.questioncloud.domain.review.QuestionReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionHubReviewRemover {
    private final QuestionReviewRepository questionReviewRepository;

    public void delete(QuestionReview questionReview) {
        questionReview.delete();
        questionReviewRepository.save(questionReview);
    }
}
