package com.eager.questioncloud.core.domain.cart.infrastructure.repository

import com.eager.questioncloud.core.domain.cart.infrastructure.entity.CartItemEntity
import org.springframework.data.jpa.repository.JpaRepository

interface CartItemJpaRepository : JpaRepository<CartItemEntity, Long> {
    fun deleteByIdInAndUserId(id: List<Long>, userId: Long)
}
