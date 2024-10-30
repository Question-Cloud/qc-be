package com.eager.questioncloud.core.domain.hub.board.implement;

import com.eager.questioncloud.core.domain.hub.board.model.QuestionBoard;
import com.eager.questioncloud.core.domain.hub.question.implement.QuestionPermissionValidator;
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
