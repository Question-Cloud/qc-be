package com.eager.questioncloud.comment;

import com.eager.questioncloud.comment.QuestionBoardCommentDto.QuestionBoardCommentDetail;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionBoardCommentService {
    private final QuestionBoardCommentAppender questionBoardCommentAppender;
    private final QuestionBoardCommentUpdater questionBoardCommentUpdater;
    private final QuestionBoardCommentRemover questionBoardCommentRemover;
    private final QuestionBoardCommentReader questionBoardCommentReader;

    public QuestionBoardComment addQuestionBoardComment(QuestionBoardComment questionBoardComment) {
        return questionBoardCommentAppender.append(questionBoardComment);
    }

    public void modifyQuestionBoardComment(Long commentId, Long userId, String comment) {
        questionBoardCommentUpdater.modify(commentId, userId, comment);
    }

    public void deleteQuestionBoardComment(Long commentId, Long userId) {
        questionBoardCommentRemover.delete(commentId, userId);
    }

    public List<QuestionBoardCommentDetail> getQuestionBoardComments(Long boardId, Long userId, Pageable pageable) {
        return questionBoardCommentReader.getQuestionBoardComments(boardId, userId, pageable);
    }

    public int count(Long boardId) {
        return questionBoardCommentReader.count(boardId);
    }
}
