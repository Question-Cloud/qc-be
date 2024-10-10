package com.eager.questioncloud.board.service;

import com.eager.questioncloud.board.domain.QuestionBoard;
import com.eager.questioncloud.board.domain.QuestionBoardFile;
import com.eager.questioncloud.board.dto.QuestionBoardDto.QuestionBoardDetail;
import com.eager.questioncloud.board.dto.QuestionBoardDto.QuestionBoardListItem;
import com.eager.questioncloud.board.implement.QuestionBoardAppender;
import com.eager.questioncloud.board.implement.QuestionBoardReader;
import com.eager.questioncloud.board.implement.QuestionBoardRemover;
import com.eager.questioncloud.board.implement.QuestionBoardUpdater;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionBoardService {
    private final QuestionBoardAppender questionBoardAppender;
    private final QuestionBoardReader questionBoardReader;
    private final QuestionBoardUpdater questionBoardUpdater;
    private final QuestionBoardRemover questionBoardRemover;

    public QuestionBoard register(QuestionBoard questionBoard) {
        return questionBoardAppender.append(questionBoard);
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
