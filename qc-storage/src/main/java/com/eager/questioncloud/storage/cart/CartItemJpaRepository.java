package com.eager.questioncloud.storage.cart;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemJpaRepository extends JpaRepository<CartItemEntity, Long> {
    List<CartItemEntity> findByUserId(Long userId);

    void deleteAllByUserId(Long userId);
}
