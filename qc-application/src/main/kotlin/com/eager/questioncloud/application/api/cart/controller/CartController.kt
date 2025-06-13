package com.eager.questioncloud.application.api.cart.controller

import com.eager.questioncloud.application.api.cart.dto.AddCartItemRequest
import com.eager.questioncloud.application.api.cart.dto.GetCartResponse
import com.eager.questioncloud.application.api.cart.dto.RemoveCartItemRequest
import com.eager.questioncloud.application.api.cart.service.CartService
import com.eager.questioncloud.application.api.common.DefaultResponse
import com.eager.questioncloud.application.security.UserPrincipal
import com.eager.questioncloud.core.domain.cart.dto.CartItemDetail
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/cart")
class CartController(
    private val cartService: CartService
) {
    @GetMapping
    fun getCart(@AuthenticationPrincipal userPrincipal: UserPrincipal): GetCartResponse {
        val items: List<CartItemDetail> = cartService.getCartItemDetails(userPrincipal.user.uid)
        return GetCartResponse.create(items)
    }

    @PostMapping
    fun addItem(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        @RequestBody request: AddCartItemRequest
    ): DefaultResponse {
        cartService.appendCartItem(userPrincipal.user.uid, request.questionId)
        return DefaultResponse.success()
    }

    @DeleteMapping
    fun removeItem(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        @RequestBody request: RemoveCartItemRequest
    ): DefaultResponse {
        cartService.removeCartItem(request.ids, userPrincipal.user.uid)
        return DefaultResponse.success()
    }
}
