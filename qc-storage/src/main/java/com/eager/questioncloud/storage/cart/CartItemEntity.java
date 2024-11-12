package com.eager.questioncloud.storage.cart;

import com.eager.questioncloud.core.domain.cart.model.CartItem;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "cart_item")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CartItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long userId;

    @Column
    private Long questionId;

    @Builder
    private CartItemEntity(Long id, Long userId, Long questionId) {
        this.id = id;
        this.userId = userId;
        this.questionId = questionId;
    }

    public static CartItemEntity from(CartItem cartItem) {
        return CartItemEntity.builder()
            .id(cartItem.getId())
            .userId(cartItem.getUserId())
            .questionId(cartItem.getItemInformation().getQuestionId())
            .build();
    }
}
