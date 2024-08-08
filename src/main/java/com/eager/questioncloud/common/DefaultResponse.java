package com.eager.questioncloud.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DefaultResponse {
    private Boolean success;

    public static DefaultResponse success() {
        return new DefaultResponse(true);
    }

}
