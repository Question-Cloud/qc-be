package com.eager.questioncloud.comment.repository;

import com.eager.questioncloud.comment.domain.QuestionBoardComment;
import com.eager.questioncloud.comment.dto.QuestionBoardCommentDto.QuestionBoardCommentDetail;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface QuestionBoardCommentRepository {
    QuestionBoardComment save(QuestionBoardComment questionBoardComment);

    QuestionBoardComment getForModifyAndDelete(Long commentId, Long userId);

    List<QuestionBoardCommentDetail> getQuestionBoardCommentDetails(Long boardId, Long userId, Pageable pageable);

    void delete(QuestionBoardComment questionBoardComment);

    int count(Long boardId);
}
