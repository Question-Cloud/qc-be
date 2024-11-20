package com.eager.questioncloud.application.api.hub;

import com.eager.questioncloud.domain.question.QuestionRepository;
import com.eager.questioncloud.domain.review.QuestionReview;
import com.eager.questioncloud.domain.review.QuestionReviewRepository;
import com.eager.questioncloud.domain.userquestion.UserQuestionRepository;
import com.eager.questioncloud.exception.CustomException;
import com.eager.questioncloud.exception.Error;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionHubReviewRegister {
    private final QuestionReviewRepository questionReviewRepository;
    private final QuestionRepository questionRepository;
    private final UserQuestionRepository userQuestionRepository;

    public void register(QuestionReview questionReview) {
        if (isUnAvailableQuestion(questionReview.getQuestionId())) {
            throw new CustomException(Error.UNAVAILABLE_QUESTION);
        }

        if (isNotOwnedQuestion(questionReview.getReviewerId(), questionReview.getQuestionId())) {
            throw new CustomException(Error.NOT_OWNED_QUESTION);
        }

        if (isAlreadyWrittenReview(questionReview.getReviewerId(), questionReview.getQuestionId())) {
            throw new CustomException(Error.ALREADY_REGISTER_REVIEW);
        }

        questionReviewRepository.save(questionReview);
    }

    private Boolean isUnAvailableQuestion(Long questionId) {
        return !questionRepository.isAvailable(questionId);
    }

    private Boolean isNotOwnedQuestion(Long userId, Long questionId) {
        return !userQuestionRepository.isOwned(userId, questionId);
    }

    private Boolean isAlreadyWrittenReview(Long userId, Long questionId) {
        return questionReviewRepository.isWritten(userId, questionId);
    }
}
