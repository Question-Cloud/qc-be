package com.eager.questioncloud.point.entity;

import com.eager.questioncloud.point.model.UserPoint;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "user_point")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserPointEntity {
    @Id
    private Long userId;

    @Column
    private int point;

    @Builder
    public UserPointEntity(Long userId, int point) {
        this.userId = userId;
        this.point = point;
    }

    public UserPoint toModel() {
        return UserPoint.builder()
            .userId(userId)
            .point(point)
            .build();
    }
}
