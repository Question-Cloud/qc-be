package com.eager.questioncloud.board;

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
