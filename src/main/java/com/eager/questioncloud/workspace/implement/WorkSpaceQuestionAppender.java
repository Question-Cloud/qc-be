package com.eager.questioncloud.workspace.implement;

import com.eager.questioncloud.question.Question;
import com.eager.questioncloud.question.QuestionContent;
import com.eager.questioncloud.question.QuestionRepository;
import com.eager.questioncloud.review.QuestionReviewStatistics;
import com.eager.questioncloud.review.QuestionReviewStatisticsAppender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WorkSpaceQuestionAppender {
    private final QuestionRepository questionRepository;
    private final QuestionReviewStatisticsAppender questionReviewStatisticsAppender;

    public Question register(Long creatorId, QuestionContent questionContent) {
        Question question = questionRepository.save(Question.create(creatorId, questionContent));
        questionReviewStatisticsAppender.append(QuestionReviewStatistics.create(question.getId()));
        return question;
    }
}
