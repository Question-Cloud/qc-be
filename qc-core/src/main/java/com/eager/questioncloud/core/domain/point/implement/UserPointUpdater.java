package com.eager.questioncloud.core.domain.point.implement;

import com.eager.questioncloud.core.domain.user.repository.UserPointRepository;
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
