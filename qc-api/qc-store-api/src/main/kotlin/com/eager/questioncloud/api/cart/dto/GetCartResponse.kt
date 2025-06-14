package com.eager.questioncloud.api.cart.dto

import com.eager.questioncloud.cart.dto.CartItemDetail

class GetCartResponse(
    val success: Boolean,
    val items: List<CartItemDetail>,
) {
    companion object {
        fun create(items: List<CartItemDetail>): GetCartResponse {
            return GetCartResponse(true, items)
        }
    }
}

