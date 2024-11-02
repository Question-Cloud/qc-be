package com.eager.questioncloud.storage.library;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LibraryJpaRepository extends JpaRepository<LibraryEntity, Long> {
    Boolean existsByUserIdAndQuestionIdIn(Long userId, List<Long> questionIds);

    Boolean existsByUserIdAndQuestionId(Long userId, Long questionId);
}
