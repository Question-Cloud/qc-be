package com.eager.questioncloud.core.domain.userquestion.infrastructure.repository;

import com.eager.questioncloud.core.domain.userquestion.infrastructure.entity.UserQuestionEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserQuestionJpaRepository extends JpaRepository<UserQuestionEntity, Long> {
    Boolean existsByUserIdAndQuestionIdIn(Long userId, List<Long> questionIds);

    Boolean existsByUserIdAndQuestionId(Long userId, Long questionId);
}
