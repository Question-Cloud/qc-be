package com.eager.questioncloud.core.domain.board.implement;

import com.eager.questioncloud.core.domain.board.model.QuestionBoard;
import com.eager.questioncloud.core.domain.board.repository.QuestionBoardRepository;
import com.eager.questioncloud.core.domain.question.implement.QuestionPermissionValidator;
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
