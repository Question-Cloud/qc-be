package com.eager.questioncloud.storage.question;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionReviewStatisticsJpaRepository extends JpaRepository<QuestionReviewStatisticsEntity, Long> {
    List<QuestionReviewStatisticsEntity> findByQuestionIdIn(List<Long> questionIds);
}
