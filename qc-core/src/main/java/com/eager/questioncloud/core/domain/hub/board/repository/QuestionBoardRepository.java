package com.eager.questioncloud.core.domain.hub.board.repository;

import com.eager.questioncloud.core.common.PagingInformation;
import com.eager.questioncloud.core.domain.hub.board.dto.PostDto.PostDetail;
import com.eager.questioncloud.core.domain.hub.board.dto.PostDto.PostListItem;
import com.eager.questioncloud.core.domain.hub.board.model.QuestionBoard;
import java.util.List;

public interface QuestionBoardRepository {
    List<PostListItem> getQuestionBoardList(Long questionId, PagingInformation pagingInformation);

    List<PostListItem> getCreatorQuestionBoardList(Long creatorId, PagingInformation pagingInformation);

    int countCreatorQuestionBoard(Long creatorId);

    PostDetail getQuestionBoardDetail(Long boardId);

    QuestionBoard findByIdAndWriterId(Long boardId, Long userId);

    QuestionBoard findById(Long boardId);

    int count(Long questionId);

    QuestionBoard save(QuestionBoard questionBoard);

    void delete(QuestionBoard questionBoard);
}
