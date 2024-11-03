package com.eager.questioncloud.core.domain.hub.board.implement;

import com.eager.questioncloud.core.domain.hub.board.model.PostComment;
import com.eager.questioncloud.core.domain.hub.board.repository.QuestionBoardCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionBoardCommentAppender {
    private final QuestionBoardCommentRepository questionBoardCommentRepository;
    private final QuestionBoardValidator questionBoardValidator;

    public PostComment append(PostComment postComment) {
        questionBoardValidator.permissionValidator(postComment.getBoardId(), postComment.getWriterId());
        return questionBoardCommentRepository.save(postComment);
    }
}
