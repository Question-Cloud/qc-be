package com.eager.questioncloud.board;

import com.eager.questioncloud.exception.CustomException;
import com.eager.questioncloud.exception.Error;
import com.eager.questioncloud.library.UserQuestionLibraryReader;
import com.eager.questioncloud.question.QuestionReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionBoardRegister {
    private final QuestionBoardAppender questionBoardAppender;
    private final UserQuestionLibraryReader userQuestionLibraryReader;
    private final QuestionReader questionReader;

    public QuestionBoard register(QuestionBoard questionBoard) {
        if (!questionReader.isAvailable(questionBoard.getQuestionId())) {
            throw new CustomException(Error.NOT_FOUND);
        }

        if (!userQuestionLibraryReader.isOwned(questionBoard.getWriterId(), questionBoard.getQuestionId())) {
            throw new CustomException(Error.NOT_OWNED_QUESTION);
        }

        return questionBoardAppender.append(questionBoard);
    }
}
