package com.eager.questioncloud.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionBoardCommentService {
    private final QuestionBoardCommentAppender questionBoardCommentAppender;

    public QuestionBoardComment addQuestionBoardComment(QuestionBoardComment questionBoardComment) {
        return questionBoardCommentAppender.append(questionBoardComment);
    }
}
