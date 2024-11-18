package com.eager.questioncloud.application.authentication;

import com.eager.questioncloud.domain.token.RefreshTokenRepository;
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
