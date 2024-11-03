package com.eager.questioncloud.core.domain.hub.board.implement;

import com.eager.questioncloud.core.domain.hub.board.model.Post;
import com.eager.questioncloud.core.domain.hub.board.repository.QuestionBoardRepository;
import com.eager.questioncloud.core.domain.hub.board.vo.PostContent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionBoardUpdater {
    private final QuestionBoardRepository questionBoardRepository;

    public void updateQuestionBoardContent(Long boardId, Long userId, PostContent postContent) {
        Post post = questionBoardRepository.findByIdAndWriterId(boardId, userId);
        post.updateQuestionBoardContent(postContent);
        questionBoardRepository.save(post);
    }
}
