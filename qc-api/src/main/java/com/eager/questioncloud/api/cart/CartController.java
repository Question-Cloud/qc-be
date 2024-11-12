package com.eager.questioncloud.api.cart;

import com.eager.questioncloud.api.cart.Request.AddCartItemRequest;
import com.eager.questioncloud.api.cart.Request.RemoveCartItemRequest;
import com.eager.questioncloud.api.cart.Response.GetCartResponse;
import com.eager.questioncloud.common.DefaultResponse;
import com.eager.questioncloud.core.domain.cart.model.CartItem;
import com.eager.questioncloud.core.domain.cart.service.CartService;
import com.eager.questioncloud.security.UserPrincipal;
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
    public GetCartResponse getCart(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        List<CartItem> items = cartService.getCartItems(userPrincipal.getUser().getUid());
        return GetCartResponse.create(items);
    }

    @PostMapping
    public DefaultResponse addItem(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody AddCartItemRequest request) {
        cartService.appendCartItem(userPrincipal.getUser().getUid(), request.getQuestionId());
        return DefaultResponse.success();
    }

    @DeleteMapping
    public DefaultResponse removeItem(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody RemoveCartItemRequest request) {
        cartService.removeCartItem(request.getIds(), userPrincipal.getUser().getUid());
        return DefaultResponse.success();
    }
}
