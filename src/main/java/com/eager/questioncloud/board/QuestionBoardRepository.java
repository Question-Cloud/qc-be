package com.eager.questioncloud.board;

import com.eager.questioncloud.board.QuestionBoardDto.QuestionBoardDetail;
import com.eager.questioncloud.board.QuestionBoardDto.QuestionBoardListItem;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface QuestionBoardRepository {
    List<QuestionBoardListItem> getQuestionBoardList(Long questionId, Pageable pageable);

    List<QuestionBoardListItem> getCreatorQuestionBoardList(Long creatorId, Pageable pageable);

    int countCreatorQuestionBoard(Long creatorId);

    QuestionBoardDetail getQuestionBoardDetail(Long boardId);

    QuestionBoard getForModifyAndDelete(Long boardId, Long userId);

    QuestionBoard get(Long boardId);

    int count(Long questionId);

    QuestionBoard save(QuestionBoard questionBoard);

    void delete(QuestionBoard questionBoard);
}
