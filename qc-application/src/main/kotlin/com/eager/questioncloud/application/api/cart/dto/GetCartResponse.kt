package com.eager.questioncloud.application.api.cart.dto

import com.eager.questioncloud.core.domain.cart.dto.CartItemDetail

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

