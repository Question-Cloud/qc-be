package com.eager.questioncloud.core.domain.hub.board.repository;

import com.eager.questioncloud.core.common.PagingInformation;
import com.eager.questioncloud.core.domain.hub.board.dto.QuestionBoardCommentDto.QuestionBoardCommentDetail;
import com.eager.questioncloud.core.domain.hub.board.model.QuestionBoardComment;
import java.util.List;

public interface QuestionBoardCommentRepository {
    QuestionBoardComment save(QuestionBoardComment questionBoardComment);

    QuestionBoardComment findByIdAndWriterId(Long commentId, Long userId);

    List<QuestionBoardCommentDetail> getQuestionBoardCommentDetails(Long boardId, Long userId, PagingInformation pagingInformation);

    void delete(QuestionBoardComment questionBoardComment);

    int count(Long boardId);
}
