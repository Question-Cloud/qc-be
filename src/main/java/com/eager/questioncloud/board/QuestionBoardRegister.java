package com.eager.questioncloud.board;

import com.eager.questioncloud.question.QuestionPermissionValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionBoardRegister {
    private final QuestionBoardAppender questionBoardAppender;
    private final QuestionPermissionValidator questionPermissionValidator;

    public QuestionBoard register(QuestionBoard questionBoard) {
        questionPermissionValidator.permissionValidator(questionBoard.getWriterId(), questionBoard.getQuestionId());
        return questionBoardAppender.append(questionBoard);
    }
}
