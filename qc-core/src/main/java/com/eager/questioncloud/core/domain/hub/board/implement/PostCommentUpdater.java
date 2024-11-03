package com.eager.questioncloud.core.domain.hub.board.implement;

import com.eager.questioncloud.core.domain.hub.board.model.PostComment;
import com.eager.questioncloud.core.domain.hub.board.repository.QuestionBoardCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostCommentUpdater {
    private final QuestionBoardCommentRepository questionBoardCommentRepository;

    public void modify(Long commentId, Long userId, String comment) {
        PostComment postComment = questionBoardCommentRepository.findByIdAndWriterId(commentId, userId);
        postComment.modify(comment);
        questionBoardCommentRepository.save(postComment);
    }
}
