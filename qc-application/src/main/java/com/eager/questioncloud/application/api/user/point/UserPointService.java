package com.eager.questioncloud.application.api.user.point;

import com.eager.questioncloud.core.domain.point.infrastructure.UserPointRepository;
import com.eager.questioncloud.core.domain.point.model.UserPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserPointService {
    private final UserPointRepository userPointRepository;

    public UserPoint getUserPoint(Long userId) {
        return userPointRepository.getUserPoint(userId);
    }
}
