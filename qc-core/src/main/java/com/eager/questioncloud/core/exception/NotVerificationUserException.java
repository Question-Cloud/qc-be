package com.eager.questioncloud.core.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NotVerificationUserException extends RuntimeException {
    private final Long userId;
}
