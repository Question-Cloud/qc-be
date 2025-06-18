package com.eager.questioncloud.cart.controller

import com.eager.questioncloud.cart.dto.AddCartItemRequest
import com.eager.questioncloud.cart.dto.CartItemDetail
import com.eager.questioncloud.cart.dto.GetCartResponse
import com.eager.questioncloud.cart.dto.RemoveCartItemRequest
import com.eager.questioncloud.cart.service.CartService
import com.eager.questioncloud.common.auth.UserPrincipal
import com.eager.questioncloud.common.dto.DefaultResponse
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/store/cart")
class CartController(
    private val cartService: CartService
) {
    @GetMapping
    fun getCart(userPrincipal: UserPrincipal): GetCartResponse {
        val items: List<CartItemDetail> = cartService.getCartItemDetails(userPrincipal.userId)
        return GetCartResponse.create(items)
    }

    @PostMapping
    fun addItem(
        userPrincipal: UserPrincipal,
        @RequestBody request: AddCartItemRequest
    ): DefaultResponse {
        cartService.appendCartItem(userPrincipal.userId, request.questionId)
        return DefaultResponse.success()
    }

    @DeleteMapping
    fun removeItem(
        userPrincipal: UserPrincipal,
        @RequestBody request: RemoveCartItemRequest
    ): DefaultResponse {
        cartService.removeCartItem(request.ids, userPrincipal.userId)
        return DefaultResponse.success()
    }
}
