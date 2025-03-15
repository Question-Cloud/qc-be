package com.eager.questioncloud.core.domain.cart.infrastructure.repository

import com.eager.questioncloud.core.domain.cart.dto.CartItemDetail
import com.eager.questioncloud.core.domain.cart.infrastructure.entity.CartItemEntity.Companion.from
import com.eager.questioncloud.core.domain.cart.infrastructure.entity.QCartItemEntity.cartItemEntity
import com.eager.questioncloud.core.domain.cart.model.CartItem
import com.eager.questioncloud.core.domain.creator.infrastructure.entity.QCreatorEntity.creatorEntity
import com.eager.questioncloud.core.domain.question.infrastructure.entity.QQuestionEntity.questionEntity
import com.eager.questioncloud.core.domain.user.infrastructure.entity.QUserEntity.userEntity
import com.querydsl.core.types.Projections
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

    override fun findByUserId(userId: Long): List<CartItemDetail> {
        return jpaQueryFactory.select(
            Projections.constructor(
                CartItemDetail::class.java,
                cartItemEntity.id,
                questionEntity.id,
                questionEntity.questionContentEntity.title,
                questionEntity.questionContentEntity.thumbnail,
                userEntity.userInformationEntity.name,
                questionEntity.questionContentEntity.subject,
                questionEntity.questionContentEntity.price
            )
        )
            .from(cartItemEntity)
            .where(cartItemEntity.userId.eq(userId))
            .leftJoin(questionEntity).on(questionEntity.id.eq(cartItemEntity.questionId))
            .leftJoin(creatorEntity).on(creatorEntity.id.eq(questionEntity.creatorId))
            .leftJoin(userEntity).on(userEntity.uid.eq(creatorEntity.userId))
            .orderBy(cartItemEntity.id.desc())
            .fetch()
    }

    @Transactional
    override fun deleteByIdInAndUserId(ids: List<Long>, userId: Long) {
        cartItemJpaRepository.deleteByIdInAndUserId(ids, userId)
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
