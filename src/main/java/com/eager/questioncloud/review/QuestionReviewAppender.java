package com.eager.questioncloud.review;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionReviewAppender {
    private final QuestionReviewRepository questionReviewRepository;
    private final QuestionReviewValidator questionReviewValidator;

    public QuestionReview append(QuestionReview questionReview) {
        questionReviewValidator.validate(questionReview);
        return questionReviewRepository.save(questionReview);
    }
}
