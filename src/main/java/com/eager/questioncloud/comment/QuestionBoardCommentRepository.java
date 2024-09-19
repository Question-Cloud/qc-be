package com.eager.questioncloud.comment;

import com.eager.questioncloud.comment.QuestionBoardCommentDto.QuestionBoardCommentDetail;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface QuestionBoardCommentRepository {
    QuestionBoardComment save(QuestionBoardComment questionBoardComment);

    QuestionBoardComment getForModifyAndDelete(Long commentId, Long userId);

    List<QuestionBoardCommentDetail> getQuestionBoardCommentDetails(Long boardId, Pageable pageable);

    void delete(QuestionBoardComment questionBoardComment);

    int count(Long boardId);
}
