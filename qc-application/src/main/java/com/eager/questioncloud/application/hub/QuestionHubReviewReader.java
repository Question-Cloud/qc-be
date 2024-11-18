package com.eager.questioncloud.application.hub;

import com.eager.questioncloud.common.PagingInformation;
import com.eager.questioncloud.domain.review.MyQuestionReview;
import com.eager.questioncloud.domain.review.QuestionReview;
import com.eager.questioncloud.domain.review.QuestionReviewDetail;
import com.eager.questioncloud.domain.review.QuestionReviewRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionHubReviewReader {
    private final QuestionReviewRepository questionReviewRepository;

    public int getTotal(Long questionId) {
        return questionReviewRepository.getTotal(questionId);
    }

    public List<QuestionReviewDetail> getQuestionReviews(Long questionId, Long userId, PagingInformation pagingInformation) {
        return questionReviewRepository.getQuestionReviews(questionId, userId, pagingInformation);
    }

    public MyQuestionReview getMyQuestionReview(Long questionId, Long userId) {
        QuestionReview questionReview = questionReviewRepository.getMyQuestionReview(questionId, userId);
        return MyQuestionReview.from(questionReview);
    }

    public Boolean isWritten(Long questionId, Long userId) {
        return questionReviewRepository.isWritten(questionId, userId);
    }

    public QuestionReview findByIdAndUserId(Long id, Long userId) {
        return questionReviewRepository.findByIdAndUserId(id, userId);
    }
}
