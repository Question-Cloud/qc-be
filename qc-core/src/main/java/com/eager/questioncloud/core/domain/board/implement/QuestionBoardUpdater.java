package com.eager.questioncloud.core.domain.board.implement;

import com.eager.questioncloud.core.domain.board.model.QuestionBoard;
import com.eager.questioncloud.core.domain.board.repository.QuestionBoardRepository;
import com.eager.questioncloud.core.domain.board.vo.QuestionBoardContent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionBoardUpdater {
    private final QuestionBoardRepository questionBoardRepository;

    public void updateQuestionBoardContent(Long boardId, Long userId, QuestionBoardContent questionBoardContent) {
        QuestionBoard questionBoard = questionBoardRepository.findByIdAndWriterId(boardId, userId);
        questionBoard.updateQuestionBoardContent(questionBoardContent);
        questionBoardRepository.save(questionBoard);
    }
}
