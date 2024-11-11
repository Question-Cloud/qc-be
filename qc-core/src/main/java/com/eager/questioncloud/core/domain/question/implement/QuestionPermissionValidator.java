package com.eager.questioncloud.core.domain.question.implement;

import com.eager.questioncloud.core.domain.library.implement.UserQuestionReader;
import com.eager.questioncloud.core.exception.CustomException;
import com.eager.questioncloud.core.exception.Error;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionPermissionValidator {
    private final QuestionReader questionReader;
    private final UserQuestionReader userQuestionReader;

    public void permissionValidator(Long userId, Long questionId) {
        if (!questionReader.isAvailable(questionId)) {
            throw new CustomException(Error.NOT_FOUND);
        }
        if (!userQuestionReader.isOwned(userId, questionId)) {
            throw new CustomException(Error.NOT_OWNED_QUESTION);
        }
    }
}