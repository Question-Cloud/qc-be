package com.eager.questioncloud.core.domain.hub.board.service;

import com.eager.questioncloud.core.common.PagingInformation;
import com.eager.questioncloud.core.domain.hub.board.dto.PostCommentDto.PostCommentDetail;
import com.eager.questioncloud.core.domain.hub.board.implement.QuestionBoardCommentAppender;
import com.eager.questioncloud.core.domain.hub.board.implement.QuestionBoardCommentReader;
import com.eager.questioncloud.core.domain.hub.board.implement.QuestionBoardCommentRemover;
import com.eager.questioncloud.core.domain.hub.board.implement.QuestionBoardCommentUpdater;
import com.eager.questioncloud.core.domain.hub.board.model.PostComment;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionBoardCommentService {
    private final QuestionBoardCommentAppender questionBoardCommentAppender;
    private final QuestionBoardCommentUpdater questionBoardCommentUpdater;
    private final QuestionBoardCommentRemover questionBoardCommentRemover;
    private final QuestionBoardCommentReader questionBoardCommentReader;

    public PostComment addQuestionBoardComment(PostComment postComment) {
        return questionBoardCommentAppender.append(postComment);
    }

    public void modifyQuestionBoardComment(Long commentId, Long userId, String comment) {
        questionBoardCommentUpdater.modify(commentId, userId, comment);
    }

    public void deleteQuestionBoardComment(Long commentId, Long userId) {
        questionBoardCommentRemover.delete(commentId, userId);
    }

    public List<PostCommentDetail> getQuestionBoardComments(Long boardId, Long userId, PagingInformation pagingInformation) {
        return questionBoardCommentReader.getQuestionBoardComments(boardId, userId, pagingInformation);
    }

    public int count(Long boardId) {
        return questionBoardCommentReader.count(boardId);
    }
}
