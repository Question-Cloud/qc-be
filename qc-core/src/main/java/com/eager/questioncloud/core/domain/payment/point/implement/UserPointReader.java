package com.eager.questioncloud.core.domain.payment.point.implement;

import com.eager.questioncloud.core.domain.payment.point.model.UserPoint;
import com.eager.questioncloud.core.domain.user.repository.UserPointRepository;
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
