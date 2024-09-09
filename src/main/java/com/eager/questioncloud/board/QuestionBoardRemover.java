package com.eager.questioncloud.board;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionBoardRemover {
    private final QuestionBoardRepository questionBoardRepository;

    public void delete(Long boardId, Long userId) {
        QuestionBoard questionBoard = questionBoardRepository.getForModifyAndDelete(boardId, userId);
        questionBoardRepository.delete(questionBoard);
    }
}
