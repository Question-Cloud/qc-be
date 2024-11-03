package com.eager.questioncloud.core.domain.hub.board.implement;

import com.eager.questioncloud.core.domain.hub.board.model.PostComment;
import com.eager.questioncloud.core.domain.hub.board.repository.QuestionBoardCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostCommentRemover {
    private final QuestionBoardCommentRepository questionBoardCommentRepository;

    public void delete(Long commentId, Long userId) {
        PostComment postComment = questionBoardCommentRepository.findByIdAndWriterId(commentId, userId);
        questionBoardCommentRepository.delete(postComment);
    }
}
