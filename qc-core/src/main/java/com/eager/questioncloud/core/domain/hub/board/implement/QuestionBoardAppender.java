package com.eager.questioncloud.core.domain.hub.board.implement;

import com.eager.questioncloud.core.domain.hub.board.model.QuestionBoard;
import com.eager.questioncloud.core.domain.hub.board.repository.QuestionBoardRepository;
import com.eager.questioncloud.core.domain.hub.question.implement.QuestionPermissionValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionBoardAppender {
    private final QuestionBoardRepository questionBoardRepository;
    private final QuestionPermissionValidator questionPermissionValidator;

    public QuestionBoard append(QuestionBoard questionBoard) {
        questionPermissionValidator.permissionValidator(questionBoard.getWriterId(), questionBoard.getQuestionId());
        return questionBoardRepository.save(questionBoard);
    }
}
