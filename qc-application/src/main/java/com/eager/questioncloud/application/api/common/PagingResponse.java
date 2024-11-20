package com.eager.questioncloud.application.api.common;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PagingResponse<T> {
    private int total;
    private List<T> result;
}
