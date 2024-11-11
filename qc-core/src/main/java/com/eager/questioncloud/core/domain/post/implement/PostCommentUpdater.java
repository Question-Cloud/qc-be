package com.eager.questioncloud.core.domain.post.implement;

import com.eager.questioncloud.core.domain.post.model.PostComment;
import com.eager.questioncloud.core.domain.post.repository.PostCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostCommentUpdater {
    private final PostCommentRepository postCommentRepository;

    public void modify(Long commentId, Long userId, String comment) {
        PostComment postComment = postCommentRepository.findByIdAndWriterId(commentId, userId);
        postComment.modify(comment);
        postCommentRepository.save(postComment);
    }
}