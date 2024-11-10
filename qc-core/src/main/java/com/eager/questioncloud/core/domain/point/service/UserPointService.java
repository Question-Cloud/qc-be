package com.eager.questioncloud.core.domain.point.service;

import com.eager.questioncloud.core.domain.point.implement.UserPointReader;
import com.eager.questioncloud.core.domain.point.model.UserPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserPointService {
    private final UserPointReader userPointReader;

    public UserPoint getUserPoint(Long userId) {
        return userPointReader.getUserPoint(userId);
    }
}
