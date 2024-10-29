package com.eager.questioncloud.core.domain.authentication.implement;

import com.eager.questioncloud.core.domain.authentication.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RefreshTokenAppender {
    private final RefreshTokenRepository refreshTokenRepository;

    public void append(String token, Long uid) {
        refreshTokenRepository.save(token, uid);
    }
}
