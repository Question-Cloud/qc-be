package com.eager.questioncloud.core.domain.questionhub.board.repository;

import com.eager.questioncloud.core.common.PagingInformation;
import com.eager.questioncloud.core.domain.questionhub.board.dto.QuestionBoardDto.QuestionBoardDetail;
import com.eager.questioncloud.core.domain.questionhub.board.dto.QuestionBoardDto.QuestionBoardListItem;
import com.eager.questioncloud.core.domain.questionhub.board.model.QuestionBoard;
import java.util.List;

public interface QuestionBoardRepository {
    List<QuestionBoardListItem> getQuestionBoardList(Long questionId, PagingInformation pagingInformation);

    List<QuestionBoardListItem> getCreatorQuestionBoardList(Long creatorId, PagingInformation pagingInformation);

    int countCreatorQuestionBoard(Long creatorId);

    QuestionBoardDetail getQuestionBoardDetail(Long boardId);

    QuestionBoard findByIdAndWriterId(Long boardId, Long userId);

    QuestionBoard findById(Long boardId);

    int count(Long questionId);

    QuestionBoard save(QuestionBoard questionBoard);

    void delete(QuestionBoard questionBoard);
}
