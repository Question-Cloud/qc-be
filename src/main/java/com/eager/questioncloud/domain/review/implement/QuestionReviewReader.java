package com.eager.questioncloud.domain.review.implement;

import com.eager.questioncloud.domain.review.dto.QuestionReviewDto.QuestionReviewItem;
import com.eager.questioncloud.domain.review.model.QuestionReview;
import com.eager.questioncloud.domain.review.repository.QuestionReviewRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionReviewReader {
    private final QuestionReviewRepository questionReviewRepository;

    public int getTotal(Long questionId) {
        return questionReviewRepository.getTotal(questionId);
    }

    public List<QuestionReviewItem> getQuestionReviews(Long questionId, Long userId, Pageable pageable) {
        return questionReviewRepository.getQuestionReviews(questionId, userId, pageable);
    }

    public QuestionReview getMyQuestionReview(Long questionId, Long userId) {
        return questionReviewRepository.getMyQuestionReview(questionId, userId);
    }

    public Boolean isWritten(Long questionId, Long userId) {
        return questionReviewRepository.isWritten(questionId, userId);
    }
}
