package com.eager.questioncloud.core.domain.cart.infrastructure.repository

import com.eager.questioncloud.core.domain.cart.infrastructure.entity.CartItemEntity
import com.eager.questioncloud.core.domain.cart.infrastructure.entity.CartItemEntity.Companion.from
import com.eager.questioncloud.core.domain.cart.infrastructure.entity.QCartItemEntity.cartItemEntity
import com.eager.questioncloud.core.domain.cart.model.CartItem
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class CartItemItemRepositoryImpl(
    private val cartItemJpaRepository: CartItemJpaRepository,
    private val jpaQueryFactory: JPAQueryFactory
) : CartItemRepository {

    override fun save(cartItem: CartItem): CartItem {
        return cartItemJpaRepository.save(from(cartItem)).toModel()
    }

    override fun findByUserId(userId: Long): List<CartItem> {
        return jpaQueryFactory.select(cartItemEntity)
            .from(cartItemEntity)
            .where(cartItemEntity.userId.eq(userId))
            .orderBy(cartItemEntity.id.desc())
            .fetch()
            .stream()
            .map(CartItemEntity::toModel)
            .toList()
    }

    @Transactional
    override fun deleteByIdInAndUserId(ids: List<Long>, userId: Long) {
        cartItemJpaRepository.deleteByIdInAndUserId(ids, userId)
    }

    @Transactional
    override fun deleteByQuestionIdInAndUserId(questionIds: List<Long>, userId: Long) {
        cartItemJpaRepository.deleteByQuestionIdInAndUserId(questionIds, userId)
    }

    override fun isExistsInCart(userId: Long, questionId: Long): Boolean {
        return jpaQueryFactory.select(cartItemEntity.id)
            .from(cartItemEntity)
            .where(
                cartItemEntity.userId.eq(userId),
                cartItemEntity.questionId.eq(questionId)
            )
            .fetchFirst() != null
    }

    override fun deleteAllInBatch() {
        cartItemJpaRepository.deleteAllInBatch()
    }
}
