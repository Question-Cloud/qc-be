package com.eager.questioncloud.workspace.implement;

import com.eager.questioncloud.board.QuestionBoardDto.QuestionBoardListItem;
import com.eager.questioncloud.board.QuestionBoardRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WorkSpaceBoardReader {
    private final QuestionBoardRepository questionBoardRepository;

    public List<QuestionBoardListItem> getCreatorQuestionBoardList(Long creatorId, Pageable pageable) {
        return questionBoardRepository.getCreatorQuestionBoardList(creatorId, pageable);
    }

    public int countCreatorQuestionBoard(Long creatorId) {
        return questionBoardRepository.countCreatorQuestionBoard(creatorId);
    }
}
