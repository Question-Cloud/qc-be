package com.eager.questioncloud.review;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionReviewAppender {
    private final QuestionReviewRepository questionReviewRepository;

    public QuestionReview append(QuestionReview questionReview) {
        return questionReviewRepository.append(questionReview);
    }
}
