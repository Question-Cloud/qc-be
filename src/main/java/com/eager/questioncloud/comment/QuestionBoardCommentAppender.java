package com.eager.questioncloud.comment;

import com.eager.questioncloud.board.QuestionBoardValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionBoardCommentAppender {
    private final QuestionBoardCommentRepository questionBoardCommentRepository;
    private final QuestionBoardValidator questionBoardValidator;

    public QuestionBoardComment append(QuestionBoardComment questionBoardComment) {
        questionBoardValidator.permissionValidator(questionBoardComment.getBoardId(), questionBoardComment.getWriterId());
        return questionBoardCommentRepository.append(questionBoardComment);
    }
}
