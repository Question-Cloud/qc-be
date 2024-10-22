package com.eager.questioncloud.board.implement;

import com.eager.questioncloud.board.model.QuestionBoard;
import com.eager.questioncloud.board.repository.QuestionBoardRepository;
import com.eager.questioncloud.board.vo.QuestionBoardContent;
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
