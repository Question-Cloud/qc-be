package com.eager.questioncloud.core.domain.hub.board.repository;

import com.eager.questioncloud.core.common.PagingInformation;
import com.eager.questioncloud.core.domain.hub.board.dto.PostDto.PostDetail;
import com.eager.questioncloud.core.domain.hub.board.dto.PostDto.PostListItem;
import com.eager.questioncloud.core.domain.hub.board.model.Post;
import java.util.List;

public interface QuestionBoardRepository {
    List<PostListItem> getQuestionBoardList(Long questionId, PagingInformation pagingInformation);

    List<PostListItem> getCreatorQuestionBoardList(Long creatorId, PagingInformation pagingInformation);

    int countCreatorQuestionBoard(Long creatorId);

    PostDetail getQuestionBoardDetail(Long boardId);

    Post findByIdAndWriterId(Long boardId, Long userId);

    Post findById(Long boardId);

    int count(Long questionId);

    Post save(Post post);

    void delete(Post post);
}
