package com.eager.questioncloud.cart.infrastructure.entity

import com.eager.questioncloud.cart.domain.CartItem
import jakarta.persistence.*

@Entity
@Table(name = "cart_item")
class CartItemEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private val id: Long = 0,
    @Column private val userId: Long,
    @Column private val questionId: Long
) {
    fun toModel(): CartItem {
        return CartItem(this.id, this.userId, this.questionId)
    }

    companion object {
        @JvmStatic
        fun from(cartItem: CartItem): CartItemEntity {
            return CartItemEntity(cartItem.id, cartItem.userId, cartItem.questionId)
        }
    }
}
