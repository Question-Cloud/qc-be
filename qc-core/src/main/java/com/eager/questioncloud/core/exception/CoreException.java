package com.eager.questioncloud.core.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CoreException extends RuntimeException {
    private Error error;
}