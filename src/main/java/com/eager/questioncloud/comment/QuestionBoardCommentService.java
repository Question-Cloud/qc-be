package com.eager.questioncloud.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionBoardCommentService {
    private final QuestionBoardCommentAppender questionBoardCommentAppender;
    private final QuestionBoardCommentUpdater questionBoardCommentUpdater;
    private final QuestionBoardCommentRemover questionBoardCommentRemover;

    public QuestionBoardComment addQuestionBoardComment(QuestionBoardComment questionBoardComment) {
        return questionBoardCommentAppender.append(questionBoardComment);
    }

    public void modifyQuestionBoardComment(Long commentId, Long userId, String comment) {
        questionBoardCommentUpdater.modify(commentId, userId, comment);
    }

    public void deleteQuestionBoardComment(Long commentId, Long userId) {
        questionBoardCommentRemover.delete(commentId, userId);
    }
}
