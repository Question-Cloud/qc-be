package com.eager.questioncloud.comment.implement;

import com.eager.questioncloud.board.implement.QuestionBoardValidator;
import com.eager.questioncloud.comment.dto.QuestionBoardCommentDto.QuestionBoardCommentDetail;
import com.eager.questioncloud.comment.repository.QuestionBoardCommentRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionBoardCommentReader {
    private final QuestionBoardCommentRepository questionBoardCommentRepository;
    private final QuestionBoardValidator questionBoardValidator;

    public List<QuestionBoardCommentDetail> getQuestionBoardComments(Long boardId, Long userId, Pageable pageable) {
        questionBoardValidator.permissionValidator(boardId, userId);
        return questionBoardCommentRepository.getQuestionBoardCommentDetails(boardId, userId, pageable);
    }

    public int count(Long boardId) {
        return questionBoardCommentRepository.count(boardId);
    }
}