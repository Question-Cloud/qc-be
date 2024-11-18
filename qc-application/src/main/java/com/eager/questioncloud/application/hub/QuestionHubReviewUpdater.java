package com.eager.questioncloud.application.hub;

import com.eager.questioncloud.domain.review.QuestionReview;
import com.eager.questioncloud.domain.review.QuestionReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionHubReviewUpdater {
    private final QuestionReviewRepository questionReviewRepository;

    public void update(QuestionReview questionReview, String comment, int rate) {
        questionReview.modify(comment, rate);
        questionReviewRepository.save(questionReview);
    }
}
