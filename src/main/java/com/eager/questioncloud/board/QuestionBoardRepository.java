package com.eager.questioncloud.board;

import com.eager.questioncloud.board.QuestionBoardDto.QuestionBoardDetail;
import com.eager.questioncloud.board.QuestionBoardDto.QuestionBoardListItem;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface QuestionBoardRepository {
    QuestionBoard append(QuestionBoard questionBoard);

    List<QuestionBoardListItem> getQuestionBoardList(Long questionId, Pageable pageable);

    QuestionBoardDetail getQuestionBoardDetail(Long questionId, Long boardId);

    int count(Long questionId);
}
