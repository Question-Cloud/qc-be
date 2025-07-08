package com.eager.questioncloud.cart.dto

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

