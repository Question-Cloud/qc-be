package com.eager.questioncloud.point;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserPointUpdater {
    private final UserPointRepository userPointRepository;

    public void updateUserPoint(Long userId, int point) {
        userPointRepository.updatePoint(userId, point);
    }
}
