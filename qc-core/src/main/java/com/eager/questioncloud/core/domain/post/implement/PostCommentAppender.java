package com.eager.questioncloud.core.domain.post.implement;

import com.eager.questioncloud.core.domain.post.model.PostComment;
import com.eager.questioncloud.core.domain.post.repository.PostCommentRepository;
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
