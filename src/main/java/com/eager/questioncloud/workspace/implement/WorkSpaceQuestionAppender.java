package com.eager.questioncloud.workspace.implement;

import com.eager.questioncloud.question.model.Question;
import com.eager.questioncloud.question.repository.QuestionRepository;
import com.eager.questioncloud.question.vo.QuestionContent;
import com.eager.questioncloud.review.implement.QuestionReviewStatisticsAppender;
import com.eager.questioncloud.review.model.QuestionReviewStatistics;
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
