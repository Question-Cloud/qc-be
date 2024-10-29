package com.eager.questioncloud.core.domain.questionhub.board.implement;

import com.eager.questioncloud.core.domain.questionhub.board.model.QuestionBoard;
import com.eager.questioncloud.core.domain.questionhub.board.repository.QuestionBoardRepository;
import com.eager.questioncloud.core.domain.questionhub.board.vo.QuestionBoardContent;
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
