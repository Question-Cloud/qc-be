package com.eager.questioncloud.user;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRepositoryImpl implements RefreshTokenRepository {
    private final RedisTemplate<String, String> redisTemplate;
    public static final String REFRESH_TOKEN_KEY_PREFIX = "REFRESH-TOKEN-UID:";

    @Override
    public String get(Long uid) {
        return redisTemplate.opsForValue().get(REFRESH_TOKEN_KEY_PREFIX + uid);
    }

    @Override
    public void delete(String uid) {
        redisTemplate.delete(REFRESH_TOKEN_KEY_PREFIX + uid);
    }

    @Override
    public void save(String token, Long uid) {
        redisTemplate.opsForValue().set(REFRESH_TOKEN_KEY_PREFIX + uid, token, Duration.ofHours(24));
    }
}
