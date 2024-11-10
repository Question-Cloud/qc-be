package com.eager.questioncloud.core.domain.hub.post.implement;

import com.eager.questioncloud.core.domain.hub.post.model.PostComment;
import com.eager.questioncloud.core.domain.hub.post.repository.PostCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostCommentRemover {
    private final PostCommentRepository postCommentRepository;

    public void delete(Long commentId, Long userId) {
        PostComment postComment = postCommentRepository.findByIdAndWriterId(commentId, userId);
        postCommentRepository.delete(postComment);
    }
}
