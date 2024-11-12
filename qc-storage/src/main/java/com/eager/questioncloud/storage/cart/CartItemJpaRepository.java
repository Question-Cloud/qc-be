package com.eager.questioncloud.storage.cart;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemJpaRepository extends JpaRepository<CartItemEntity, Long> {
    void deleteByIdInAndUserId(List<Long> id, Long userId);

    void deleteByQuestionIdInAndUserId(List<Long> questionIds, Long userId);
}
