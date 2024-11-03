package com.eager.questioncloud.storage.question;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostJpaRepository extends JpaRepository<PostEntity, Long> {
    Optional<PostEntity> findByIdAndWriterId(Long id, Long userId);
}
