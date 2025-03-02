package com.eager.questioncloud.core.domain.post.infrastructure.repository;

import com.eager.questioncloud.core.domain.post.infrastructure.entity.PostCommentEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostCommentJpaRepository extends JpaRepository<PostCommentEntity, Long> {
    Optional<PostCommentEntity> findByIdAndWriterId(Long id, Long userId);
}
