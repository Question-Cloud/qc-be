package com.eager.questioncloud.core.domain.hub.review.implement;

import com.eager.questioncloud.core.common.PagingInformation;
import com.eager.questioncloud.core.domain.hub.review.dto.QuestionReviewDto.QuestionReviewItem;
import com.eager.questioncloud.core.domain.hub.review.model.QuestionReview;
import com.eager.questioncloud.core.domain.hub.review.repository.QuestionReviewRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionReviewReader {
    private final QuestionReviewRepository questionReviewRepository;

    public int getTotal(Long questionId) {
        return questionReviewRepository.getTotal(questionId);
    }

    public List<QuestionReviewItem> getQuestionReviews(Long questionId, Long userId, PagingInformation pagingInformation) {
        return questionReviewRepository.getQuestionReviews(questionId, userId, pagingInformation);
    }

    public QuestionReview getMyQuestionReview(Long questionId, Long userId) {
        return questionReviewRepository.getMyQuestionReview(questionId, userId);
    }

    public Boolean isWritten(Long questionId, Long userId) {
        return questionReviewRepository.isWritten(questionId, userId);
    }
}
