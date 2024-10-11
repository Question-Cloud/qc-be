package com.eager.questioncloud.exception;

import com.eager.questioncloud.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NotVerificationUserException extends RuntimeException {
    private final User user;
}
