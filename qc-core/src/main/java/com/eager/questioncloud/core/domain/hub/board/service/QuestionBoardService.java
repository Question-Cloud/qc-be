package com.eager.questioncloud.core.domain.hub.board.service;

import com.eager.questioncloud.core.common.PagingInformation;
import com.eager.questioncloud.core.domain.hub.board.dto.PostDto.PostDetail;
import com.eager.questioncloud.core.domain.hub.board.dto.PostDto.PostListItem;
import com.eager.questioncloud.core.domain.hub.board.implement.QuestionBoardAppender;
import com.eager.questioncloud.core.domain.hub.board.implement.QuestionBoardReader;
import com.eager.questioncloud.core.domain.hub.board.implement.QuestionBoardRemover;
import com.eager.questioncloud.core.domain.hub.board.implement.QuestionBoardUpdater;
import com.eager.questioncloud.core.domain.hub.board.model.Post;
import com.eager.questioncloud.core.domain.hub.board.vo.PostContent;
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

    public Post register(Post post) {
        return questionBoardAppender.append(post);
    }

    public List<PostListItem> getQuestionBoardList(Long userId, Long questionId, PagingInformation pagingInformation) {
        return questionBoardReader.getQuestionBoardList(userId, questionId, pagingInformation);
    }

    public int countQuestionBoard(Long questionId) {
        return questionBoardReader.count(questionId);
    }

    public PostDetail getQuestionBoardDetail(Long userId, Long boardId) {
        return questionBoardReader.getQuestionBoardDetail(userId, boardId);
    }

    public void modify(Long boardId, Long userId, PostContent postContent) {
        questionBoardUpdater.updateQuestionBoardContent(boardId, userId, postContent);
    }

    public void delete(Long boardId, Long userId) {
        questionBoardRemover.delete(boardId, userId);
    }
}
