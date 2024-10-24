package com.eager.questioncloud.core.domain.board.implement;

import com.eager.questioncloud.core.domain.board.model.QuestionBoard;
import com.eager.questioncloud.core.domain.board.repository.QuestionBoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionBoardRemover {
    private final QuestionBoardRepository questionBoardRepository;

    public void delete(Long boardId, Long userId) {
        QuestionBoard questionBoard = questionBoardRepository.findByIdAndWriterId(boardId, userId);
        questionBoardRepository.delete(questionBoard);
    }
}
