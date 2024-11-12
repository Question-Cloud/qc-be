package com.eager.questioncloud.core.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PagingInformation {
    private int offset;
    private int size;
}
