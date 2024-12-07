package com.eager.questioncloud.core.domain.point;

import org.springframework.stereotype.Repository;

@Repository
public interface UserPointRepository {
    UserPoint getUserPoint(Long userId);

    void updatePoint(Long userId, int point);

    UserPoint save(UserPoint userPoint);
}
