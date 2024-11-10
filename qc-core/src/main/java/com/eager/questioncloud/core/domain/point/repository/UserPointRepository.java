package com.eager.questioncloud.core.domain.point.repository;

import com.eager.questioncloud.core.domain.point.model.UserPoint;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPointRepository {
    UserPoint getUserPoint(Long userId);

    void updatePoint(Long userId, int point);

    UserPoint save(UserPoint userPoint);
}
