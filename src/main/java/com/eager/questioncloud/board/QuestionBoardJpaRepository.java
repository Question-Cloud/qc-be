package com.eager.questioncloud.board;

import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionBoardJpaRepository extends JpaRepository<QuestionBoardEntity, Long> {
}
