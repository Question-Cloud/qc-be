package com.eager.questioncloud.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionBoardCommentRemover {
    private final QuestionBoardCommentRepository questionBoardCommentRepository;

    public void delete(Long commentId, Long userId) {
        QuestionBoardComment questionBoardComment = questionBoardCommentRepository.getForModifyAndDelete(commentId, userId);
        questionBoardCommentRepository.delete(questionBoardComment);
    }
}
