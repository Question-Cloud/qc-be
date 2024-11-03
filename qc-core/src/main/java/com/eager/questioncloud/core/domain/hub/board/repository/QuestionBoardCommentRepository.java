package com.eager.questioncloud.core.domain.hub.board.repository;

import com.eager.questioncloud.core.common.PagingInformation;
import com.eager.questioncloud.core.domain.hub.board.dto.PostCommentDto.PostCommentDetail;
import com.eager.questioncloud.core.domain.hub.board.model.PostComment;
import java.util.List;

public interface QuestionBoardCommentRepository {
    PostComment save(PostComment postComment);

    PostComment findByIdAndWriterId(Long commentId, Long userId);

    List<PostCommentDetail> getQuestionBoardCommentDetails(Long boardId, Long userId, PagingInformation pagingInformation);

    void delete(PostComment postComment);

    int count(Long boardId);
}
