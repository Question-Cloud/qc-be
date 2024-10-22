package com.eager.questioncloud.domain.library.repository;

import com.eager.questioncloud.domain.library.entity.UserQuestionLibraryEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserQuestionLibraryJpaRepository extends JpaRepository<UserQuestionLibraryEntity, Long> {
    Boolean existsByUserIdAndQuestionIdIn(Long userId, List<Long> questionIds);

    Boolean existsByUserIdAndQuestionId(Long userId, Long questionId);
}
