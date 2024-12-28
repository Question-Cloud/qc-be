package com.eager.questioncloud.application.api.post.service;

import com.eager.questioncloud.application.api.post.implement.PostPermissionChecker;
import com.eager.questioncloud.core.common.PagingInformation;
import com.eager.questioncloud.core.domain.post.dto.PostDetail;
import com.eager.questioncloud.core.domain.post.dto.PostListItem;
import com.eager.questioncloud.core.domain.post.infrastructure.repository.PostRepository;
import com.eager.questioncloud.core.domain.post.model.Post;
import com.eager.questioncloud.core.domain.post.model.PostContent;
import com.eager.questioncloud.core.exception.CoreException;
import com.eager.questioncloud.core.exception.Error;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostPermissionChecker postPermissionChecker;
    private final PostRepository postRepository;

    public Post register(Post post) {
        if (!postPermissionChecker.hasPermission(post.getWriterId(), post.getQuestionId())) {
            throw new CoreException(Error.FORBIDDEN);
        }

        return postRepository.save(post);
    }

    public List<PostListItem> getPostList(Long userId, Long questionId, PagingInformation pagingInformation) {
        if (!postPermissionChecker.hasPermission(userId, questionId)) {
            throw new CoreException(Error.FORBIDDEN);
        }

        return postRepository.getPostList(questionId, pagingInformation);
    }

    public int countPost(Long questionId) {
        return postRepository.count(questionId);
    }

    public PostDetail getPostDetail(Long userId, Long postId) {
        if (!postPermissionChecker.hasPermission(userId, postId)) {
            throw new CoreException(Error.FORBIDDEN);
        }
        return postRepository.getPostDetail(postId);
    }

    public void modify(Long postId, Long userId, PostContent postContent) {
        Post post = postRepository.findByIdAndWriterId(postId, userId);
        post.updateQuestionBoardContent(postContent);
        postRepository.save(post);
    }

    public void delete(Long postId, Long userId) {
        Post post = postRepository.findByIdAndWriterId(postId, userId);
        postRepository.delete(post);
    }
}
