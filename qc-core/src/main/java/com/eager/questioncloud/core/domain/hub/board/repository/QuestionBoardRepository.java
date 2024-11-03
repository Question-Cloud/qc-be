package com.eager.questioncloud.core.domain.hub.board.repository;

import com.eager.questioncloud.core.common.PagingInformation;
import com.eager.questioncloud.core.domain.hub.board.dto.PostDto.QuestionBoardDetail;
import com.eager.questioncloud.core.domain.hub.board.dto.PostDto.QuestionBoardListItem;
import com.eager.questioncloud.core.domain.hub.board.model.QuestionBoard;
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
