package com.eager.questioncloud.board;

import com.eager.questioncloud.board.QuestionBoardDto.QuestionBoardListItem;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionBoardService {
    private final QuestionBoardRegister questionBoardRegister;
    private final QuestionBoardReader questionBoardReader;

    public QuestionBoard register(QuestionBoard questionBoard) {
        return questionBoardRegister.register(questionBoard);
    }

    public List<QuestionBoardListItem> getQuestionBoardList(Long userId, Long questionId, Pageable pageable) {
        return questionBoardReader.getQuestionBoardList(userId, questionId, pageable);
    }

    public int countQuestionBoard(Long questionId) {
        return questionBoardReader.count(questionId);
    }
}
