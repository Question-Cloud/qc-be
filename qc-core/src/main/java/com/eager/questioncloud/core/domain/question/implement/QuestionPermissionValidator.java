package com.eager.questioncloud.core.domain.question.implement;

import com.eager.questioncloud.exception.CustomException;
import com.eager.questioncloud.exception.Error;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionPermissionValidator {
    private final QuestionReader questionReader;
    //TODO libaray 도메인 추가 후 처리
//    private final UserQuestionLibraryReader userQuestionLibraryReader;

    public void permissionValidator(Long userId, Long questionId) {
        if (!questionReader.isAvailable(questionId)) {
            throw new CustomException(Error.NOT_FOUND);
        }
//        if (!userQuestionLibraryReader.isOwned(userId, questionId)) {
//            throw new CustomException(Error.NOT_OWNED_QUESTION);
//        }
    }
}
