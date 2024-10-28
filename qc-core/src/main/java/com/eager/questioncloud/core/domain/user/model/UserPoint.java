package com.eager.questioncloud.core.domain.user.model;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserPoint {
    private Long userId;
    private int point;

    @Builder
    public UserPoint(Long userId, int point) {
        this.userId = userId;
        this.point = point;
    }
}
