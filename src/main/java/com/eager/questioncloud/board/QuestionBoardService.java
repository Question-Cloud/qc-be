package com.eager.questioncloud.board;

import com.eager.questioncloud.board.QuestionBoardDto.QuestionBoardDetail;
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
    private final QuestionBoardUpdater questionBoardUpdater;
    private final QuestionBoardRemover questionBoardRemover;

    public QuestionBoard register(QuestionBoard questionBoard) {
        return questionBoardRegister.register(questionBoard);
    }

    public List<QuestionBoardListItem> getQuestionBoardList(Long userId, Long questionId, Pageable pageable) {
        return questionBoardReader.getQuestionBoardList(userId, questionId, pageable);
    }

    public int countQuestionBoard(Long questionId) {
        return questionBoardReader.count(questionId);
    }

    public QuestionBoardDetail getQuestionBoardDetail(Long userId, Long boardId) {
        return questionBoardReader.getQuestionBoardDetail(userId, boardId);
    }

    public void modify(Long boardId, Long userId, String title, String content, List<QuestionBoardFile> files) {
        questionBoardUpdater.modify(boardId, userId, title, content, files);
    }

    public void delete(Long boardId, Long userId) {
        questionBoardRemover.delete(boardId, userId);
    }
}
