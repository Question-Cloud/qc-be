package com.eager.questioncloud.authentication.implement;

import com.eager.questioncloud.authentication.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RefreshTokenReader {
    private final RefreshTokenRepository refreshTokenRepository;

    public String getByUserId(Long userId) {
        return refreshTokenRepository.getByUserId(userId);
    }
}
