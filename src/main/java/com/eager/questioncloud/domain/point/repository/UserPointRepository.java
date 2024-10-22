package com.eager.questioncloud.domain.point.repository;

import org.springframework.stereotype.Repository;

@Repository
public interface UserPointRepository {
    int getPoint(Long userId);

    void updatePoint(Long userId, int point);
}
