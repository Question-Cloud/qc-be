package com.eager.questioncloud.question.implement;

import com.eager.questioncloud.exception.CustomException;
import com.eager.questioncloud.exception.Error;
import com.eager.questioncloud.library.UserQuestionLibraryReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionPermissionValidator {
    private final QuestionReader questionReader;
    private final UserQuestionLibraryReader userQuestionLibraryReader;

    public void permissionValidator(Long userId, Long questionId) {
        if (!questionReader.isAvailable(questionId)) {
            throw new CustomException(Error.NOT_FOUND);
        }
        if (!userQuestionLibraryReader.isOwned(userId, questionId)) {
            throw new CustomException(Error.NOT_OWNED_QUESTION);
        }
    }
}
