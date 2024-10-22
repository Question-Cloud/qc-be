package com.eager.questioncloud.domain.point.service;

import com.eager.questioncloud.domain.point.implement.UserPointReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserPointService {
    private final UserPointReader userPointReader;

    public int getUserPoint(Long userId) {
        return userPointReader.getUserPoint(userId);
    }
}
