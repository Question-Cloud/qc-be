package com.eager.questioncloud.core.domain.hub.board.service;

import com.eager.questioncloud.core.common.PagingInformation;
import com.eager.questioncloud.core.domain.hub.board.dto.PostCommentDto.PostCommentDetail;
import com.eager.questioncloud.core.domain.hub.board.implement.PostCommentAppender;
import com.eager.questioncloud.core.domain.hub.board.implement.PostCommentReader;
import com.eager.questioncloud.core.domain.hub.board.implement.PostCommentRemover;
import com.eager.questioncloud.core.domain.hub.board.implement.PostCommentUpdater;
import com.eager.questioncloud.core.domain.hub.board.model.PostComment;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionBoardCommentService {
    private final PostCommentAppender postCommentAppender;
    private final PostCommentUpdater postCommentUpdater;
    private final PostCommentRemover postCommentRemover;
    private final PostCommentReader postCommentReader;

    public PostComment addQuestionBoardComment(PostComment postComment) {
        return postCommentAppender.append(postComment);
    }

    public void modifyQuestionBoardComment(Long commentId, Long userId, String comment) {
        postCommentUpdater.modify(commentId, userId, comment);
    }

    public void deleteQuestionBoardComment(Long commentId, Long userId) {
        postCommentRemover.delete(commentId, userId);
    }

    public List<PostCommentDetail> getQuestionBoardComments(Long boardId, Long userId, PagingInformation pagingInformation) {
        return postCommentReader.getPostComments(boardId, userId, pagingInformation);
    }

    public int count(Long boardId) {
        return postCommentReader.count(boardId);
    }
}
