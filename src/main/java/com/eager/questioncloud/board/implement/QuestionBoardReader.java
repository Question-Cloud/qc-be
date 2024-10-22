package com.eager.questioncloud.board.implement;

import com.eager.questioncloud.board.dto.QuestionBoardDto.QuestionBoardDetail;
import com.eager.questioncloud.board.dto.QuestionBoardDto.QuestionBoardListItem;
import com.eager.questioncloud.board.model.QuestionBoard;
import com.eager.questioncloud.board.repository.QuestionBoardRepository;
import com.eager.questioncloud.question.implement.QuestionPermissionValidator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
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

    public List<QuestionBoardListItem> getQuestionBoardList(Long userId, Long questionId, Pageable pageable) {
        questionPermissionValidator.permissionValidator(userId, questionId);
        return questionBoardRepository.getQuestionBoardList(questionId, pageable);
    }

    public int count(Long questionId) {
        return questionBoardRepository.count(questionId);
    }
}
