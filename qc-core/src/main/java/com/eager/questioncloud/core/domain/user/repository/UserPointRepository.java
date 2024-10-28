package com.eager.questioncloud.core.domain.user.repository;

import org.springframework.stereotype.Repository;

@Repository
public interface UserPointRepository {
    int getPoint(Long userId);

    void updatePoint(Long userId, int point);
}
