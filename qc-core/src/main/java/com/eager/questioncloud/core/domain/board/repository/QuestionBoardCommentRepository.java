package com.eager.questioncloud.core.domain.board.repository;

import com.eager.questioncloud.core.common.PagingInformation;
import com.eager.questioncloud.core.domain.board.dto.QuestionBoardCommentDto.QuestionBoardCommentDetail;
import com.eager.questioncloud.core.domain.board.model.QuestionBoardComment;
import java.util.List;

public interface QuestionBoardCommentRepository {
    QuestionBoardComment save(QuestionBoardComment questionBoardComment);

    QuestionBoardComment findByIdAndWriterId(Long commentId, Long userId);

    List<QuestionBoardCommentDetail> getQuestionBoardCommentDetails(Long boardId, Long userId, PagingInformation pagingInformation);

    void delete(QuestionBoardComment questionBoardComment);

    int count(Long boardId);
}
