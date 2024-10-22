package com.eager.questioncloud.domain.workspace.implement;

import com.eager.questioncloud.domain.question.model.Question;
import com.eager.questioncloud.domain.question.repository.QuestionRepository;
import com.eager.questioncloud.domain.question.vo.QuestionContent;
import com.eager.questioncloud.domain.review.implement.QuestionReviewStatisticsAppender;
import com.eager.questioncloud.domain.review.model.QuestionReviewStatistics;
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
