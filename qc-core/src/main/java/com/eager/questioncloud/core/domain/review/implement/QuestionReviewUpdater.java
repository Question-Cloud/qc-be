package com.eager.questioncloud.core.domain.review.implement;

import com.eager.questioncloud.core.domain.review.model.QuestionReview;
import com.eager.questioncloud.core.domain.review.repository.QuestionReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionReviewUpdater {
    private final QuestionReviewRepository questionReviewRepository;

    public void update(QuestionReview questionReview, String comment, int rate) {
        questionReview.modify(comment, rate);
        questionReviewRepository.save(questionReview);
    }
}
