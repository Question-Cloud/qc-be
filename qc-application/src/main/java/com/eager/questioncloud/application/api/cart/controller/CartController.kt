package com.eager.questioncloud.application.api.cart.controller

import com.eager.questioncloud.application.api.cart.dto.AddCartItemRequest
import com.eager.questioncloud.application.api.cart.dto.GetCartResponse
import com.eager.questioncloud.application.api.cart.dto.RemoveCartItemRequest
import com.eager.questioncloud.application.api.common.DefaultResponse
import com.eager.questioncloud.application.business.cart.service.CartService
import com.eager.questioncloud.application.security.UserPrincipal
import com.eager.questioncloud.core.domain.cart.dto.CartItemDetail
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/cart")
class CartController(
    private val cartService: CartService
) {
    @GetMapping
    @ApiResponses(value = [ApiResponse(responseCode = "200", description = "요청 성공")])
    @Operation(operationId = "장바구니 조회", summary = "장바구니 조회", tags = ["cart"], description = "장바구니 조회")
    fun getCart(@AuthenticationPrincipal userPrincipal: UserPrincipal): GetCartResponse {
        val items: List<CartItemDetail> = cartService.getCartItems(userPrincipal.user.uid!!)
        return GetCartResponse.create(items)
    }

    @PostMapping
    @ApiResponses(value = [ApiResponse(responseCode = "200", description = "요청 성공")])
    @Operation(operationId = "장바구니 담기", summary = "장바구니 담기", tags = ["cart"], description = "장바구니 담기")
    fun addItem(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        @RequestBody request: AddCartItemRequest
    ): DefaultResponse {
        cartService.appendCartItem(userPrincipal.user.uid!!, request.questionId)
        return DefaultResponse.success()
    }

    @DeleteMapping
    @ApiResponses(value = [ApiResponse(responseCode = "200", description = "요청 성공")])
    @Operation(operationId = "장바구니 빼기", summary = "장바구니 빼기", tags = ["cart"], description = "장바구니 빼기")
    fun removeItem(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        @RequestBody request: RemoveCartItemRequest
    ): DefaultResponse {
        cartService.removeCartItem(request.ids, userPrincipal.user.uid!!)
        return DefaultResponse.success()
    }
}
