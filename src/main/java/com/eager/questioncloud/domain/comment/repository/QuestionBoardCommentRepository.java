package com.eager.questioncloud.domain.comment.repository;

import com.eager.questioncloud.domain.comment.dto.QuestionBoardCommentDto.QuestionBoardCommentDetail;
import com.eager.questioncloud.domain.comment.model.QuestionBoardComment;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface QuestionBoardCommentRepository {
    QuestionBoardComment save(QuestionBoardComment questionBoardComment);

    QuestionBoardComment findByIdAndWriterId(Long commentId, Long userId);

    List<QuestionBoardCommentDetail> getQuestionBoardCommentDetails(Long boardId, Long userId, Pageable pageable);

    void delete(QuestionBoardComment questionBoardComment);

    int count(Long boardId);
}
