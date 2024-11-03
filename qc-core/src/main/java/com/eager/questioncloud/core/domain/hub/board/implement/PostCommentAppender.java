package com.eager.questioncloud.core.domain.hub.board.implement;

import com.eager.questioncloud.core.domain.hub.board.model.PostComment;
import com.eager.questioncloud.core.domain.hub.board.repository.QuestionBoardCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostCommentAppender {
    private final QuestionBoardCommentRepository questionBoardCommentRepository;
    private final PostValidator postValidator;

    public PostComment append(PostComment postComment) {
        postValidator.permissionValidator(postComment.getPostId(), postComment.getWriterId());
        return questionBoardCommentRepository.save(postComment);
    }
}
