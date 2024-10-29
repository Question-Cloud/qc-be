package com.eager.questioncloud.core.exception;

import com.eager.questioncloud.core.domain.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NotVerificationUserException extends RuntimeException {
    private final User user;
}
