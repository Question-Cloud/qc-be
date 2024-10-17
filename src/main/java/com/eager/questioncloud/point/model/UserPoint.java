package com.eager.questioncloud.point.model;

import com.eager.questioncloud.point.entity.UserPointEntity;
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

    public UserPointEntity toEntity() {
        return UserPointEntity.builder()
            .userId(userId)
            .point(point)
            .build();
    }
}
