package com.eager.questioncloud.board.implement;

import com.eager.questioncloud.board.domain.QuestionBoard;
import com.eager.questioncloud.board.repository.QuestionBoardRepository;
import com.eager.questioncloud.question.implement.QuestionPermissionValidator;
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
