package com.eager.questioncloud.core.domain.post.service;

import com.eager.questioncloud.core.common.PagingInformation;
import com.eager.questioncloud.core.domain.post.dto.PostCommentDetail;
import com.eager.questioncloud.core.domain.post.implement.PostCommentAppender;
import com.eager.questioncloud.core.domain.post.implement.PostCommentReader;
import com.eager.questioncloud.core.domain.post.implement.PostCommentRemover;
import com.eager.questioncloud.core.domain.post.implement.PostCommentUpdater;
import com.eager.questioncloud.core.domain.post.model.PostComment;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostCommentService {
    private final PostCommentAppender postCommentAppender;
    private final PostCommentUpdater postCommentUpdater;
    private final PostCommentRemover postCommentRemover;
    private final PostCommentReader postCommentReader;

    public PostComment addPostComment(PostComment postComment) {
        return postCommentAppender.append(postComment);
    }

    public void modifyPostComment(Long commentId, Long userId, String comment) {
        postCommentUpdater.modify(commentId, userId, comment);
    }

    public void deletePostComment(Long commentId, Long userId) {
        postCommentRemover.delete(commentId, userId);
    }

    public List<PostCommentDetail> getPostComments(Long postId, Long userId, PagingInformation pagingInformation) {
        return postCommentReader.getPostComments(postId, userId, pagingInformation);
    }

    public int count(Long postId) {
        return postCommentReader.count(postId);
    }
}
