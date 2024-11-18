package com.eager.questioncloud.application.authentication;

import com.eager.questioncloud.domain.token.RefreshTokenRepository;
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
