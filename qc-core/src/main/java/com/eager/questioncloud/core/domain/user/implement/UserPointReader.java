package com.eager.questioncloud.core.domain.user.implement;

import com.eager.questioncloud.core.domain.user.repository.UserPointRepository;
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
