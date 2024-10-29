package com.eager.questioncloud.core.domain.questionhub.board.implement;

import com.eager.questioncloud.core.domain.questionhub.board.model.QuestionBoardComment;
import com.eager.questioncloud.core.domain.questionhub.board.repository.QuestionBoardCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionBoardCommentUpdater {
    private final QuestionBoardCommentRepository questionBoardCommentRepository;

    public void modify(Long commentId, Long userId, String comment) {
        QuestionBoardComment questionBoardComment = questionBoardCommentRepository.findByIdAndWriterId(commentId, userId);
        questionBoardComment.modify(comment);
        questionBoardCommentRepository.save(questionBoardComment);
    }
}
