package com.eager.questioncloud.comment;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionBoardCommentJpaRepository extends JpaRepository<QuestionBoardCommentEntity, Long> {
    Optional<QuestionBoardCommentEntity> findByIdAndWriterId(Long id, Long userId);
}
