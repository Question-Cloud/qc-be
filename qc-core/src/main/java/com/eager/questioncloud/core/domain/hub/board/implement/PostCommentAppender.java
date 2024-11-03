package com.eager.questioncloud.core.domain.hub.board.implement;

import com.eager.questioncloud.core.domain.hub.board.model.PostComment;
import com.eager.questioncloud.core.domain.hub.board.repository.PostCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostCommentAppender {
    private final PostCommentRepository postCommentRepository;
    private final PostValidator postValidator;

    public PostComment append(PostComment postComment) {
        postValidator.permissionValidator(postComment.getPostId(), postComment.getWriterId());
        return postCommentRepository.save(postComment);
    }
}
