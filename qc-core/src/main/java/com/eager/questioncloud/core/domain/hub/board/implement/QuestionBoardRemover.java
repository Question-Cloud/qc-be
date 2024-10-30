package com.eager.questioncloud.core.domain.hub.board.implement;

import com.eager.questioncloud.core.domain.hub.board.model.QuestionBoard;
import com.eager.questioncloud.core.domain.hub.board.repository.QuestionBoardRepository;
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