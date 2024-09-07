package com.eager.questioncloud.review;

import com.eager.questioncloud.review.QuestionReviewDto.MyQuestionReview;
import com.eager.questioncloud.review.QuestionReviewDto.QuestionReviewItem;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionReviewService {
    private final QuestionReviewReader questionReviewReader;
    private final QuestionReviewRegister questionReviewRegister;

    public int getTotal(Long questionId) {
        return questionReviewReader.getTotal(questionId);
    }

    public List<QuestionReviewItem> getQuestionReviews(Long questionId, Long userId, Pageable pageable) {
        return questionReviewReader.getQuestionReviews(questionId, userId, pageable);
    }

    public MyQuestionReview getMyQuestionReview(Long questionId, Long userId) {
        return questionReviewReader.getMyQuestionReview(questionId, userId);
    }

    public QuestionReview register(QuestionReview questionReview) {
        return questionReviewRegister.register(questionReview);
    }
}
