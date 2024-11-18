package com.eager.questioncloud.application.cart;

import com.eager.questioncloud.application.cart.Request.AddCartItemRequest;
import com.eager.questioncloud.application.cart.Request.RemoveCartItemRequest;
import com.eager.questioncloud.application.cart.Response.GetCartResponse;
import com.eager.questioncloud.application.common.DefaultResponse;
import com.eager.questioncloud.application.security.UserPrincipal;
import com.eager.questioncloud.domain.cart.CartItemDetail;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @GetMapping
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청 성공")
    })
    @Operation(operationId = "장바구니 조회", summary = "장바구니 조회", tags = {"cart"}, description = "장바구니 조회")
    public GetCartResponse getCart(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        List<CartItemDetail> items = cartService.getCartItems(userPrincipal.getUser().getUid());
        return GetCartResponse.create(items);
    }

    @PostMapping
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청 성공")
    })
    @Operation(operationId = "장바구니 담기", summary = "장바구니 담기", tags = {"cart"}, description = "장바구니 담기")
    public DefaultResponse addItem(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody AddCartItemRequest request) {
        cartService.appendCartItem(userPrincipal.getUser().getUid(), request.getQuestionId());
        return DefaultResponse.success();
    }

    @DeleteMapping
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청 성공")
    })
    @Operation(operationId = "장바구니 빼기", summary = "장바구니 빼기", tags = {"cart"}, description = "장바구니 빼기")
    public DefaultResponse removeItem(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody RemoveCartItemRequest request) {
        cartService.removeCartItem(request.getIds(), userPrincipal.getUser().getUid());
        return DefaultResponse.success();
    }
}
