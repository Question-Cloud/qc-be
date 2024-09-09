package com.eager.questioncloud.board;


import com.eager.questioncloud.question.QuestionPermissionValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionBoardValidator {
    private final QuestionBoardReader questionBoardReader;
    private final QuestionPermissionValidator questionPermissionValidator;

    public void permissionValidator(Long boardId, Long userId) {
        QuestionBoard questionBoard = questionBoardReader.get(boardId);
        questionPermissionValidator.permissionValidator(userId, questionBoard.getQuestionId());
    }
}
