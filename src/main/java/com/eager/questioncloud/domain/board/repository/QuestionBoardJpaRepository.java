package com.eager.questioncloud.domain.board.repository;

import com.eager.questioncloud.domain.board.entity.QuestionBoardEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionBoardJpaRepository extends JpaRepository<QuestionBoardEntity, Long> {
    Optional<QuestionBoardEntity> findByIdAndWriterId(Long id, Long userId);
}
