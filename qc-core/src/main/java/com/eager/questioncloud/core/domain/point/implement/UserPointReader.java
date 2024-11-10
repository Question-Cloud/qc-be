package com.eager.questioncloud.core.domain.point.implement;

import com.eager.questioncloud.core.domain.point.model.UserPoint;
import com.eager.questioncloud.core.domain.point.repository.UserPointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserPointReader {
    private final UserPointRepository userPointRepository;

    public UserPoint getUserPoint(Long userId) {
        return userPointRepository.getUserPoint(userId);
    }
}
