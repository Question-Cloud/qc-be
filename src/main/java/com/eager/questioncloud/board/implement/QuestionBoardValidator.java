package com.eager.questioncloud.board.implement;


import com.eager.questioncloud.board.model.QuestionBoard;
import com.eager.questioncloud.question.implement.QuestionPermissionValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionBoardValidator {
    private final QuestionBoardReader questionBoardReader;
    private final QuestionPermissionValidator questionPermissionValidator;

    public void permissionValidator(Long boardId, Long userId) {
        QuestionBoard questionBoard = questionBoardReader.findById(boardId);
        questionPermissionValidator.permissionValidator(userId, questionBoard.getQuestionId());
    }
}
