package com.eager.questioncloud.point.service;

import com.eager.questioncloud.point.implement.UserPointReader;
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
