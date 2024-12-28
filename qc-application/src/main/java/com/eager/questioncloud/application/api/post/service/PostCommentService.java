package com.eager.questioncloud.application.api.post.service;

import com.eager.questioncloud.application.api.post.implement.PostPermissionChecker;
import com.eager.questioncloud.core.common.PagingInformation;
import com.eager.questioncloud.core.domain.post.dto.PostCommentDetail;
import com.eager.questioncloud.core.domain.post.infrastructure.repository.PostCommentRepository;
import com.eager.questioncloud.core.domain.post.model.PostComment;
import com.eager.questioncloud.core.exception.CoreException;
import com.eager.questioncloud.core.exception.Error;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostCommentService {
    private final PostPermissionChecker postPermissionChecker;
    private final PostCommentRepository postCommentRepository;

    public PostComment addPostComment(PostComment postComment) {
        if (!postPermissionChecker.hasCommentPermission(postComment.getWriterId(), postComment.getPostId())) {
            throw new CoreException(Error.FORBIDDEN);
        }
        return postCommentRepository.save(postComment);
    }

    public void modifyPostComment(Long commentId, Long userId, String comment) {
        PostComment postComment = postCommentRepository.findByIdAndWriterId(commentId, userId);
        postComment.modify(comment);
        postCommentRepository.save(postComment);
    }

    public void deletePostComment(Long commentId, Long userId) {
        PostComment postComment = postCommentRepository.findByIdAndWriterId(commentId, userId);
        postCommentRepository.delete(postComment);
    }

    public List<PostCommentDetail> getPostComments(Long postId, Long userId, PagingInformation pagingInformation) {
        if (!postPermissionChecker.hasCommentPermission(userId, postId)) {
            throw new CoreException(Error.FORBIDDEN);
        }
        return postCommentRepository.getPostCommentDetails(postId, userId, pagingInformation);
    }

    public int count(Long postId) {
        return postCommentRepository.count(postId);
    }
}
