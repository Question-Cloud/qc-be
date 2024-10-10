package com.eager.questioncloud.board.implement;

import com.eager.questioncloud.board.domain.QuestionBoard;
import com.eager.questioncloud.board.domain.QuestionBoardFile;
import com.eager.questioncloud.board.repository.QuestionBoardRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionBoardUpdater {
    private final QuestionBoardRepository questionBoardRepository;

    public void modify(Long boardId, Long userId, String title, String content, List<QuestionBoardFile> files) {
        QuestionBoard questionBoard = questionBoardRepository.getForModifyAndDelete(boardId, userId);
        questionBoard.modify(title, content, files);
        questionBoardRepository.save(questionBoard);
    }
}
