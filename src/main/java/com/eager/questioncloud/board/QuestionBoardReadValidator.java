package com.eager.questioncloud.board;


import com.eager.questioncloud.exception.CustomException;
import com.eager.questioncloud.exception.Error;
import com.eager.questioncloud.library.UserQuestionLibraryReader;
import com.eager.questioncloud.question.QuestionReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionBoardReadValidator {
    private final QuestionReader questionReader;
    private final UserQuestionLibraryReader userQuestionLibraryReader;

    public void questionBoardListValidator(Long userId, Long questionId) {
        if (!questionReader.isAvailable(questionId)) {
            throw new CustomException(Error.NOT_FOUND);
        }
        if (!userQuestionLibraryReader.isOwned(userId, questionId)) {
            throw new CustomException(Error.NOT_OWNED_QUESTION);
        }
    }

    public void accessValidator(Long userId, Long questionId) {
        if (!questionReader.isAvailable(questionId)) {
            throw new CustomException(Error.NOT_FOUND);
        }
        if (!userQuestionLibraryReader.isOwned(userId, questionId)) {
            throw new CustomException(Error.NOT_OWNED_QUESTION);
        }
    }
}
