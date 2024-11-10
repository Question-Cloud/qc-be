package com.eager.questioncloud.core.domain.review.implement;

import com.eager.questioncloud.core.domain.review.model.QuestionReview;
import com.eager.questioncloud.core.domain.review.repository.QuestionReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionReviewRegister {
    private final QuestionReviewRepository questionReviewRepository;
    private final QuestionReviewValidator questionReviewValidator;

    public QuestionReview register(QuestionReview questionReview) {
        questionReviewValidator.validate(questionReview);
        questionReviewRepository.save(questionReview);
        return questionReview;
    }
}
