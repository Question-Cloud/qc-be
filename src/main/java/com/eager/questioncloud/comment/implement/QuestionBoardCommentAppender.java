package com.eager.questioncloud.comment.implement;

import com.eager.questioncloud.board.implement.QuestionBoardValidator;
import com.eager.questioncloud.comment.model.QuestionBoardComment;
import com.eager.questioncloud.comment.repository.QuestionBoardCommentRepository;
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
