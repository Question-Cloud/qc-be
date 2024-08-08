package com.eager.questioncloud.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final AuthenticationTokenProcessor authenticationTokenProcessor;

    public AuthenticationToken generateAuthenticateToken(Long uid) {
        return new AuthenticationToken(
            authenticationTokenProcessor.generateAccessToken(uid),
            authenticationTokenProcessor.generateRefreshToken(uid));
    }
}
