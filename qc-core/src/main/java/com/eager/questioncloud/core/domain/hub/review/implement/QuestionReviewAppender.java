package com.eager.questioncloud.core.domain.hub.review.implement;

import com.eager.questioncloud.core.domain.hub.review.model.QuestionReview;
import com.eager.questioncloud.core.domain.hub.review.repository.QuestionReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class QuestionReviewAppender {
    private final QuestionReviewRepository questionReviewRepository;
    private final QuestionReviewValidator questionReviewValidator;

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public QuestionReview append(QuestionReview questionReview) {
        questionReviewValidator.validate(questionReview);
        questionReviewRepository.save(questionReview);
        return questionReview;
    }
}
