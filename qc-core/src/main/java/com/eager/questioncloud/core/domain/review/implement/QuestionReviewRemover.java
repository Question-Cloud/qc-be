package com.eager.questioncloud.core.domain.review.implement;

import com.eager.questioncloud.core.domain.review.model.QuestionReview;
import com.eager.questioncloud.core.domain.review.repository.QuestionReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionReviewRemover {
    private final QuestionReviewRepository questionReviewRepository;

    public void delete(QuestionReview questionReview) {
        questionReview.delete();
        questionReviewRepository.save(questionReview);
    }
}
