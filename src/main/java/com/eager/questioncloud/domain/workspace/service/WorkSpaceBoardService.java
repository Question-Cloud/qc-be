package com.eager.questioncloud.domain.workspace.service;

import com.eager.questioncloud.domain.board.dto.QuestionBoardDto.QuestionBoardListItem;
import com.eager.questioncloud.domain.workspace.implement.WorkSpaceBoardReader;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WorkSpaceBoardService {
    private final WorkSpaceBoardReader workSpaceBoardReader;

    public List<QuestionBoardListItem> getCreatorQuestionBoardList(Long creatorId, Pageable pageable) {
        return workSpaceBoardReader.getCreatorQuestionBoardList(creatorId, pageable);
    }

    public int countCreatorQuestionBoardList(Long creatorId) {
        return workSpaceBoardReader.countCreatorQuestionBoard(creatorId);
    }
}
