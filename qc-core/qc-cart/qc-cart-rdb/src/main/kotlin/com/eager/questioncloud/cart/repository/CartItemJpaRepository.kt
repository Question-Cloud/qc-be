package com.eager.questioncloud.cart.repository

import com.eager.questioncloud.cart.entity.CartItemEntity
import org.springframework.data.jpa.repository.JpaRepository

interface CartItemJpaRepository : JpaRepository<CartItemEntity, Long> {
    fun deleteByIdInAndUserId(id: List<Long>, userId: Long)
    
    fun deleteByQuestionIdInAndUserId(questionIds: List<Long>, userId: Long)
}
