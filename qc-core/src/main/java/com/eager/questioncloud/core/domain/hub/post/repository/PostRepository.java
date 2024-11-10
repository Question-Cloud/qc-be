package com.eager.questioncloud.core.domain.hub.post.repository;

import com.eager.questioncloud.core.common.PagingInformation;
import com.eager.questioncloud.core.domain.hub.post.dto.PostDto.PostDetail;
import com.eager.questioncloud.core.domain.hub.post.dto.PostDto.PostListItem;
import com.eager.questioncloud.core.domain.hub.post.model.Post;
import java.util.List;

public interface PostRepository {
    List<PostListItem> getPostList(Long questionId, PagingInformation pagingInformation);

    List<PostListItem> getCreatorPostList(Long creatorId, PagingInformation pagingInformation);

    int countCreatorPost(Long creatorId);

    PostDetail getPostDetail(Long postId);

    Post findByIdAndWriterId(Long postId, Long userId);

    Post findById(Long postId);

    int count(Long questionId);

    Post save(Post post);

    void delete(Post post);
}
