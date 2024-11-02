package com.eager.questioncloud.core.domain.hub.question.implement;

import com.eager.questioncloud.core.domain.hub.question.model.Question;
import com.eager.questioncloud.core.domain.hub.question.repository.QuestionRepository;
import com.eager.questioncloud.core.domain.hub.question.vo.QuestionContent;
import com.eager.questioncloud.core.domain.hub.review.model.QuestionReviewStatistics;
import com.eager.questioncloud.core.domain.hub.review.repository.QuestionReviewStatisticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class QuestionRegister {
    private final QuestionRepository questionRepository;
    private final QuestionReviewStatisticsRepository questionReviewStatisticsRepository;

    @Transactional
    public Question register(Long creatorId, QuestionContent questionContent) {
        Question question = questionRepository.save(Question.create(creatorId, questionContent));
        questionReviewStatisticsRepository.save(QuestionReviewStatistics.create(question.getId()));
        return question;
    }
}
