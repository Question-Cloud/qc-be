package com.eager.questioncloud.core.domain.questionhub.board.repository;

import com.eager.questioncloud.core.common.PagingInformation;
import com.eager.questioncloud.core.domain.questionhub.board.dto.QuestionBoardCommentDto.QuestionBoardCommentDetail;
import com.eager.questioncloud.core.domain.questionhub.board.model.QuestionBoardComment;
import java.util.List;

public interface QuestionBoardCommentRepository {
    QuestionBoardComment save(QuestionBoardComment questionBoardComment);

    QuestionBoardComment findByIdAndWriterId(Long commentId, Long userId);

    List<QuestionBoardCommentDetail> getQuestionBoardCommentDetails(Long boardId, Long userId, PagingInformation pagingInformation);

    void delete(QuestionBoardComment questionBoardComment);

    int count(Long boardId);
}
