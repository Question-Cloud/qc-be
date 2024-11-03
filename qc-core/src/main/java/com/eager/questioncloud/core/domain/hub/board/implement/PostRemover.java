package com.eager.questioncloud.core.domain.hub.board.implement;

import com.eager.questioncloud.core.domain.hub.board.model.Post;
import com.eager.questioncloud.core.domain.hub.board.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostRemover {
    private final PostRepository postRepository;

    public void delete(Long boardId, Long userId) {
        Post post = postRepository.findByIdAndWriterId(boardId, userId);
        postRepository.delete(post);
    }
}
