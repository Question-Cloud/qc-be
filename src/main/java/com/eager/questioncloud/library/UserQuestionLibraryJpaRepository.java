package com.eager.questioncloud.library;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserQuestionLibraryJpaRepository extends JpaRepository<UserQuestionLibraryEntity, Long> {
    Boolean existsByUserIdAndQuestionIdIn(Long userId, List<Long> questionIds);
}
