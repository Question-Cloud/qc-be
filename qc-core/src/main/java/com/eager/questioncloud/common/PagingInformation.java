package com.eager.questioncloud.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PagingInformation {
    private int offset;
    private int size;
}
