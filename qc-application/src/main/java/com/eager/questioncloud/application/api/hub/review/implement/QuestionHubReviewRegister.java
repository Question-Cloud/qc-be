package com.eager.questioncloud.application.api.hub.review.implement;

import com.eager.questioncloud.core.domain.question.infrastructure.QuestionRepository;
import com.eager.questioncloud.core.domain.review.infrastructure.QuestionReviewRepository;
import com.eager.questioncloud.core.domain.review.model.QuestionReview;
import com.eager.questioncloud.core.domain.userquestion.infrastructure.UserQuestionRepository;
import com.eager.questioncloud.exception.CustomException;
import com.eager.questioncloud.exception.Error;
import com.eager.questioncloud.lock.LockKeyGenerator;
import com.eager.questioncloud.lock.LockManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionHubReviewRegister {
    private final QuestionReviewRepository questionReviewRepository;
    private final QuestionRepository questionRepository;
    private final UserQuestionRepository userQuestionRepository;
    private final LockManager lockManager;

    public void register(QuestionReview questionReview) {
        lockManager.executeWithLock(
            LockKeyGenerator.generateRegisterReview(questionReview.getReviewerId(), questionReview.getQuestionId()),
            () -> {
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
        );
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
