package com.eager.questioncloud.point;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserPointReader {
    private final UserPointRepository userPointRepository;

    public int getUserPoint(Long userId) {
        return userPointRepository.getPoint(userId);
    }
}
