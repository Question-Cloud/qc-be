package com.eager.questioncloud.core.domain.post.infrastructure;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostJpaRepository extends JpaRepository<PostEntity, Long> {
    Optional<PostEntity> findByIdAndWriterId(Long id, Long userId);
}
