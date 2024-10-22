package com.eager.questioncloud.domain.comment.implement;

import com.eager.questioncloud.domain.comment.model.QuestionBoardComment;
import com.eager.questioncloud.domain.comment.repository.QuestionBoardCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionBoardCommentRemover {
    private final QuestionBoardCommentRepository questionBoardCommentRepository;

    public void delete(Long commentId, Long userId) {
        QuestionBoardComment questionBoardComment = questionBoardCommentRepository.findByIdAndWriterId(commentId, userId);
        questionBoardCommentRepository.delete(questionBoardComment);
    }
}
