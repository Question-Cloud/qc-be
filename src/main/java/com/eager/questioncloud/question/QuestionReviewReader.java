package com.eager.questioncloud.question;

import com.eager.questioncloud.question.QuestionReviewDto.QuestionReviewItem;
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
}
