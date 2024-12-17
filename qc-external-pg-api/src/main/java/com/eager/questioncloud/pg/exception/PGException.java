package com.eager.questioncloud.pg.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PGException extends RuntimeException {
    private final String message;
}
