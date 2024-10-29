package com.eager.questioncloud.core.domain.board.implement;

import com.eager.questioncloud.core.domain.board.model.QuestionBoardComment;
import com.eager.questioncloud.core.domain.board.repository.QuestionBoardCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionBoardCommentAppender {
    private final QuestionBoardCommentRepository questionBoardCommentRepository;
    private final QuestionBoardValidator questionBoardValidator;

    public QuestionBoardComment append(QuestionBoardComment questionBoardComment) {
        questionBoardValidator.permissionValidator(questionBoardComment.getBoardId(), questionBoardComment.getWriterId());
        return questionBoardCommentRepository.save(questionBoardComment);
    }
}
