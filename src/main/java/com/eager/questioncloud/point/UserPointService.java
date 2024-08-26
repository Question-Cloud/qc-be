package com.eager.questioncloud.point;

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
