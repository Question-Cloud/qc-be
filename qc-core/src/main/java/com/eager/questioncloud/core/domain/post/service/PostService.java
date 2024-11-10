package com.eager.questioncloud.core.domain.post.service;

import com.eager.questioncloud.core.common.PagingInformation;
import com.eager.questioncloud.core.domain.post.dto.PostDto.PostDetail;
import com.eager.questioncloud.core.domain.post.dto.PostDto.PostListItem;
import com.eager.questioncloud.core.domain.post.implement.PostAppender;
import com.eager.questioncloud.core.domain.post.implement.PostReader;
import com.eager.questioncloud.core.domain.post.implement.PostRemover;
import com.eager.questioncloud.core.domain.post.implement.PostUpdater;
import com.eager.questioncloud.core.domain.post.model.Post;
import com.eager.questioncloud.core.domain.post.vo.PostContent;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostAppender postAppender;
    private final PostReader postReader;
    private final PostUpdater postUpdater;
    private final PostRemover postRemover;

    public Post register(Post post) {
        return postAppender.append(post);
    }

    public List<PostListItem> getPostList(Long userId, Long questionId, PagingInformation pagingInformation) {
        return postReader.getPosts(userId, questionId, pagingInformation);
    }

    public int countPost(Long questionId) {
        return postReader.count(questionId);
    }

    public PostDetail getPostDetail(Long userId, Long postId) {
        return postReader.getPostDetail(userId, postId);
    }

    public void modify(Long postId, Long userId, PostContent postContent) {
        postUpdater.updatePost(postId, userId, postContent);
    }

    public void delete(Long postId, Long userId) {
        postRemover.delete(postId, userId);
    }
}
