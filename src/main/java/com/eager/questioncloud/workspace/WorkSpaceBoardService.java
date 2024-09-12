package com.eager.questioncloud.workspace;

import com.eager.questioncloud.board.QuestionBoardDto.QuestionBoardListItem;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WorkSpaceBoardService {
    private final WorkSpaceBoardReader workSpaceBoardReader;

    public List<QuestionBoardListItem> getCreatorQuestionBoardList(Long userId, Pageable pageable) {
        return workSpaceBoardReader.getCreatorQuestionBoardList(userId, pageable);
    }

    public int countCreatorQuestionBoardList(Long userId) {
        return workSpaceBoardReader.countCreatorQuestionBoard(userId);
    }
}
