package com.eager.questioncloud.board;

import com.eager.questioncloud.board.QuestionBoardDto.QuestionBoardDetail;
import com.eager.questioncloud.board.QuestionBoardDto.QuestionBoardListItem;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionBoardReader {
    private final QuestionBoardRepository questionBoardRepository;
    private final QuestionBoardValidator questionBoardValidator;

    public QuestionBoardDetail getQuestionBoardDetail(Long userId, Long questionId, Long boardId) {
        questionBoardValidator.accessValidator(userId, questionId);
        return questionBoardRepository.getQuestionBoardDetail(questionId, boardId);
    }

    public List<QuestionBoardListItem> getQuestionBoardList(Long userId, Long questionId, Pageable pageable) {
        questionBoardValidator.accessValidator(userId, questionId);
        return questionBoardRepository.getQuestionBoardList(questionId, pageable);
    }

    public int count(Long questionId) {
        return questionBoardRepository.count(questionId);
    }
}
