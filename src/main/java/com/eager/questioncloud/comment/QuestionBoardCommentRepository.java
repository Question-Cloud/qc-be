package com.eager.questioncloud.comment;

public interface QuestionBoardCommentRepository {
    QuestionBoardComment append(QuestionBoardComment questionBoardComment);

    QuestionBoardComment save(QuestionBoardComment questionBoardComment);

    QuestionBoardComment getForModifyAndDelete(Long commentId, Long userId);

    void delete(QuestionBoardComment questionBoardComment);
}
