package com.eager.questioncloud.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionBoardCommentUpdater {
    private final QuestionBoardCommentRepository questionBoardCommentRepository;

    public void modify(Long commentId, Long userId, String comment) {
        QuestionBoardComment questionBoardComment = questionBoardCommentRepository.getForModifyAndDelete(commentId, userId);
        questionBoardComment.modify(comment);
        questionBoardCommentRepository.save(questionBoardComment);
    }
}
