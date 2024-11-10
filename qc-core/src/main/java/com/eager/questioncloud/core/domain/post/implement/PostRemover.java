package com.eager.questioncloud.core.domain.post.implement;

import com.eager.questioncloud.core.domain.post.model.Post;
import com.eager.questioncloud.core.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostRemover {
    private final PostRepository postRepository;

    public void delete(Long postId, Long userId) {
        Post post = postRepository.findByIdAndWriterId(postId, userId);
        postRepository.delete(post);
    }
}
