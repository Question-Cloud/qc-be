package com.eager.questioncloud.storage.cart;

import static com.eager.questioncloud.storage.cart.QCartItemEntity.cartItemEntity;
import static com.eager.questioncloud.storage.creator.QCreatorEntity.creatorEntity;
import static com.eager.questioncloud.storage.question.QQuestionEntity.questionEntity;
import static com.eager.questioncloud.storage.user.QUserEntity.userEntity;

import com.eager.questioncloud.core.domain.cart.model.CartItem;
import com.eager.questioncloud.core.domain.cart.repository.CartItemRepository;
import com.eager.questioncloud.core.domain.cart.vo.CartItemInformation;
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
        CartItemEntity cartItemEntity = cartItemJpaRepository.save(CartItemEntity.from(cartItem));
        return CartItem.builder()
            .id(cartItemEntity.getId())
            .userId(cartItem.getUserId())
            .itemInformation(cartItem.getItemInformation())
            .build();
    }

    @Override
    public List<CartItem> findByUserId(Long userId) {
        return jpaQueryFactory.select(
                Projections.constructor(CartItem.class,
                    cartItemEntity.id,
                    cartItemEntity.userId,
                    Projections.constructor(CartItemInformation.class, questionEntity.id, questionEntity.questionContentEntity.title,
                        questionEntity.questionContentEntity.thumbnail, userEntity.userInformationEntity.name,
                        questionEntity.questionContentEntity.subject, questionEntity.questionContentEntity.price)
                )
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
