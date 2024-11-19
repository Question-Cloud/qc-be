package com.eager.questioncloud.application.post;

import com.eager.questioncloud.common.PagingInformation;
import com.eager.questioncloud.domain.post.Post;
import com.eager.questioncloud.domain.post.PostContent;
import com.eager.questioncloud.domain.post.PostDetail;
import com.eager.questioncloud.domain.post.PostListItem;
import com.eager.questioncloud.domain.post.PostRepository;
import com.eager.questioncloud.exception.CustomException;
import com.eager.questioncloud.exception.Error;
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
            throw new CustomException(Error.FORBIDDEN);
        }

        return postRepository.save(post);
    }

    public List<PostListItem> getPostList(Long userId, Long questionId, PagingInformation pagingInformation) {
        if (!postPermissionChecker.hasPermission(userId, questionId)) {
            throw new CustomException(Error.FORBIDDEN);
        }

        return postRepository.getPostList(questionId, pagingInformation);
    }

    public int countPost(Long questionId) {
        return postRepository.count(questionId);
    }

    public PostDetail getPostDetail(Long userId, Long postId) {
        if (!postPermissionChecker.hasPermission(userId, postId)) {
            throw new CustomException(Error.FORBIDDEN);
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
