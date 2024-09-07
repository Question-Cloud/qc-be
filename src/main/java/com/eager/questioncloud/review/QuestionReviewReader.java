package com.eager.questioncloud.review;

import com.eager.questioncloud.exception.CustomException;
import com.eager.questioncloud.exception.Error;
import com.eager.questioncloud.review.QuestionReviewDto.MyQuestionReview;
import com.eager.questioncloud.review.QuestionReviewDto.QuestionReviewItem;
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

    public MyQuestionReview getMyQuestionReview(Long questionId, Long userId) {
        MyQuestionReview myQuestionReview = questionReviewRepository.getMyQuestionReview(questionId, userId);
        if (myQuestionReview == null) {
            throw new CustomException(Error.NOT_FOUND);
        }
        return myQuestionReview;
    }

    public Boolean isWritten(Long questionId, Long userId) {
        return questionReviewRepository.isWritten(questionId, userId);
    }
}
