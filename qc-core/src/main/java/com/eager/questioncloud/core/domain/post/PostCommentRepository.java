package com.eager.questioncloud.core.domain.post;

import com.eager.questioncloud.core.common.PagingInformation;
import java.util.List;

public interface PostCommentRepository {
    PostComment save(PostComment postComment);

    PostComment findByIdAndWriterId(Long commentId, Long userId);

    List<PostCommentDetail> getPostCommentDetails(Long postId, Long userId, PagingInformation pagingInformation);

    void delete(PostComment postComment);

    int count(Long postId);
}
