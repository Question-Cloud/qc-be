package com.eager.questioncloud.core.domain.hub.board.implement;

import com.eager.questioncloud.core.common.PagingInformation;
import com.eager.questioncloud.core.domain.hub.board.dto.PostCommentDto.PostCommentDetail;
import com.eager.questioncloud.core.domain.hub.board.repository.QuestionBoardCommentRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionBoardCommentReader {
    private final QuestionBoardCommentRepository questionBoardCommentRepository;
    private final PostValidator postValidator;

    public List<PostCommentDetail> getQuestionBoardComments(Long boardId, Long userId, PagingInformation pagingInformation) {
        postValidator.permissionValidator(boardId, userId);
        return questionBoardCommentRepository.getQuestionBoardCommentDetails(boardId, userId, pagingInformation);
    }

    public int count(Long boardId) {
        return questionBoardCommentRepository.count(boardId);
    }
}
