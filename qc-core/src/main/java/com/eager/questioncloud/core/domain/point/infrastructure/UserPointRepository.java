package com.eager.questioncloud.core.domain.point.infrastructure;

import com.eager.questioncloud.core.domain.point.model.UserPoint;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPointRepository {
    UserPoint getUserPoint(Long userId);

    Boolean usePoint(Long userId, int amount);

    void chargePoint(Long userId, int amount);

    UserPoint save(UserPoint userPoint);
}
