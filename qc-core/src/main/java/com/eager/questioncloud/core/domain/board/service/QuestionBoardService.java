package com.eager.questioncloud.core.domain.board.service;

import com.eager.questioncloud.core.common.PagingInformation;
import com.eager.questioncloud.core.domain.board.dto.QuestionBoardDto.QuestionBoardDetail;
import com.eager.questioncloud.core.domain.board.dto.QuestionBoardDto.QuestionBoardListItem;
import com.eager.questioncloud.core.domain.board.implement.QuestionBoardAppender;
import com.eager.questioncloud.core.domain.board.implement.QuestionBoardReader;
import com.eager.questioncloud.core.domain.board.implement.QuestionBoardRemover;
import com.eager.questioncloud.core.domain.board.implement.QuestionBoardUpdater;
import com.eager.questioncloud.core.domain.board.model.QuestionBoard;
import com.eager.questioncloud.core.domain.board.vo.QuestionBoardContent;
import java.util.List;
import lombok.RequiredArgsConstructor;
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

    public List<QuestionBoardListItem> getQuestionBoardList(Long userId, Long questionId, PagingInformation pagingInformation) {
        return questionBoardReader.getQuestionBoardList(userId, questionId, pagingInformation);
    }

    public int countQuestionBoard(Long questionId) {
        return questionBoardReader.count(questionId);
    }

    public QuestionBoardDetail getQuestionBoardDetail(Long userId, Long boardId) {
        return questionBoardReader.getQuestionBoardDetail(userId, boardId);
    }

    public void modify(Long boardId, Long userId, QuestionBoardContent questionBoardContent) {
        questionBoardUpdater.updateQuestionBoardContent(boardId, userId, questionBoardContent);
    }

    public void delete(Long boardId, Long userId) {
        questionBoardRemover.delete(boardId, userId);
    }
}
