package com.eager.questioncloud.core.domain.cart.infrastructure.repository;


import static com.eager.questioncloud.core.domain.cart.infrastructure.entity.QCartItemEntity.cartItemEntity;
import static com.eager.questioncloud.core.domain.creator.infrastructure.QCreatorEntity.creatorEntity;
import static com.eager.questioncloud.core.domain.question.infrastructure.QQuestionEntity.questionEntity;
import static com.eager.questioncloud.core.domain.user.infrastructure.QUserEntity.userEntity;

import com.eager.questioncloud.core.domain.cart.dto.CartItemDetail;
import com.eager.questioncloud.core.domain.cart.infrastructure.entity.CartItemEntity;
import com.eager.questioncloud.core.domain.cart.model.CartItem;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CartItemItemRepositoryImpl implements CartItemRepository {
    private final CartItemJpaRepository cartItemJpaRepository;
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public CartItem save(CartItem cartItem) {
        return cartItemJpaRepository.save(CartItemEntity.from(cartItem)).toModel();
    }

    @Override
    public List<CartItemDetail> findByUserId(Long userId) {
        return jpaQueryFactory.select(
                Projections.constructor(
                    CartItemDetail.class,
                    questionEntity.id, questionEntity.id, questionEntity.questionContentEntity.title,
                    questionEntity.questionContentEntity.thumbnail, userEntity.userInformationEntity.name,
                    questionEntity.questionContentEntity.subject, questionEntity.questionContentEntity.price)
            )
            .from(cartItemEntity)
            .leftJoin(questionEntity).on(questionEntity.id.eq(cartItemEntity.questionId))
            .leftJoin(creatorEntity).on(creatorEntity.id.eq(questionEntity.creatorId))
            .leftJoin(userEntity).on(userEntity.uid.eq(creatorEntity.userId))
            .fetch();
    }

    @Override
    public void deleteByIdInAndUserId(List<Long> ids, Long userId) {
        cartItemJpaRepository.deleteByIdInAndUserId(ids, userId);
    }

    @Override
    public void deleteByQuestionIdInAndUserId(List<Long> questionIds, Long userId) {
        cartItemJpaRepository.deleteByQuestionIdInAndUserId(questionIds, userId);
    }

    @Override
    public Boolean isExistsInCart(Long userId, Long questionId) {
        return jpaQueryFactory.select(cartItemEntity.id)
            .from(cartItemEntity)
            .where(cartItemEntity.userId.eq(userId), cartItemEntity.questionId.eq(questionId))
            .fetchFirst() != null;
    }
}
