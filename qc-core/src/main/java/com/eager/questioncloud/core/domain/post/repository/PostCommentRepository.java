package com.eager.questioncloud.core.domain.post.repository;

import com.eager.questioncloud.core.common.PagingInformation;
import com.eager.questioncloud.core.domain.post.dto.PostCommentDto.PostCommentDetail;
import com.eager.questioncloud.core.domain.post.model.PostComment;
import java.util.List;

public interface PostCommentRepository {
    PostComment save(PostComment postComment);

    PostComment findByIdAndWriterId(Long commentId, Long userId);

    List<PostCommentDetail> getPostCommentDetails(Long postId, Long userId, PagingInformation pagingInformation);

    void delete(PostComment postComment);

    int count(Long postId);
}
