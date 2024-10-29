package com.eager.questioncloud.core.domain.questionhub.review.repository;

import com.eager.questioncloud.core.common.PagingInformation;
import com.eager.questioncloud.core.domain.questionhub.review.dto.QuestionReviewDto.QuestionReviewItem;
import com.eager.questioncloud.core.domain.questionhub.review.model.QuestionReview;
import java.util.List;

public interface QuestionReviewRepository {
    int getTotal(Long questionId);

    List<QuestionReviewItem> getQuestionReviews(Long questionId, Long userId, PagingInformation pagingInformation);

    QuestionReview getMyQuestionReview(Long questionId, Long userId);

    QuestionReview getForModifyAndDelete(Long reviewId, Long userId);

    Boolean isWritten(Long questionId, Long userId);

    QuestionReview save(QuestionReview questionReview);
}
