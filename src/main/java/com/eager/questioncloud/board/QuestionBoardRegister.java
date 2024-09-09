package com.eager.questioncloud.board;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionBoardRegister {
    private final QuestionBoardAppender questionBoardAppender;
    private final QuestionBoardValidator questionBoardValidator;

    public QuestionBoard register(QuestionBoard questionBoard) {
        questionBoardValidator.permissionValidator(questionBoard.getWriterId(), questionBoard.getQuestionId());
        return questionBoardAppender.append(questionBoard);
    }
}
