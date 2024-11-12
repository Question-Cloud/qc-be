package com.eager.questioncloud.core.domain.cart.implement;

import com.eager.questioncloud.core.domain.cart.model.CartItem;
import com.eager.questioncloud.core.domain.cart.repository.CartItemRepository;
import com.eager.questioncloud.core.domain.library.repository.LibraryRepository;
import com.eager.questioncloud.core.exception.CustomException;
import com.eager.questioncloud.core.exception.Error;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CartItemAppender {
    private final CartItemRepository cartItemRepository;
    private final LibraryRepository libraryRepository;

    public void append(CartItem cartItem) {
        if (libraryRepository.isOwned(cartItem.getUserId(), cartItem.getItemInformation().getQuestionId())) {
            throw new CustomException(Error.ALREADY_OWN_QUESTION);
        }

        cartItemRepository.save(cartItem);
    }
}
