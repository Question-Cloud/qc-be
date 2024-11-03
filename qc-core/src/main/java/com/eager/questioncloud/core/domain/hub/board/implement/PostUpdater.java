package com.eager.questioncloud.core.domain.hub.board.implement;

import com.eager.questioncloud.core.domain.hub.board.model.Post;
import com.eager.questioncloud.core.domain.hub.board.repository.PostRepository;
import com.eager.questioncloud.core.domain.hub.board.vo.PostContent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostUpdater {
    private final PostRepository postRepository;

    public void updateQuestionBoardContent(Long boardId, Long userId, PostContent postContent) {
        Post post = postRepository.findByIdAndWriterId(boardId, userId);
        post.updateQuestionBoardContent(postContent);
        postRepository.save(post);
    }
}
