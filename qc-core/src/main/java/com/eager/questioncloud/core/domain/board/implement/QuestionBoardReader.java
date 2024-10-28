package com.eager.questioncloud.core.domain.board.implement;

import com.eager.questioncloud.core.common.PagingInformation;
import com.eager.questioncloud.core.domain.board.dto.QuestionBoardDto.QuestionBoardDetail;
import com.eager.questioncloud.core.domain.board.dto.QuestionBoardDto.QuestionBoardListItem;
import com.eager.questioncloud.core.domain.board.model.QuestionBoard;
import com.eager.questioncloud.core.domain.board.repository.QuestionBoardRepository;
import com.eager.questioncloud.core.domain.question.implement.QuestionPermissionValidator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionBoardReader {
    private final QuestionBoardRepository questionBoardRepository;
    private final QuestionPermissionValidator questionPermissionValidator;

    public QuestionBoard findById(Long boardId) {
        return questionBoardRepository.findById(boardId);
    }

    public QuestionBoardDetail getQuestionBoardDetail(Long userId, Long boardId) {
        QuestionBoardDetail questionBoard = questionBoardRepository.getQuestionBoardDetail(boardId);
        questionPermissionValidator.permissionValidator(userId, questionBoard.getQuestionId());
        return questionBoard;
    }

    public List<QuestionBoardListItem> getQuestionBoardList(Long userId, Long questionId, PagingInformation pagingInformation) {
        questionPermissionValidator.permissionValidator(userId, questionId);
        return questionBoardRepository.getQuestionBoardList(questionId, pagingInformation);
    }

    public int count(Long questionId) {
        return questionBoardRepository.count(questionId);
    }

    public List<QuestionBoardListItem> getCreatorQuestionBoardList(Long creatorId, PagingInformation pagingInformation) {
        return questionBoardRepository.getCreatorQuestionBoardList(creatorId, pagingInformation);
    }

    public int countCreatorQuestionBoard(Long creatorId) {
        return questionBoardRepository.countCreatorQuestionBoard(creatorId);
    }
}
