package com.eager.questioncloud.domain.token;

public interface RefreshTokenRepository {
    String getByUserId(Long uid);

    void delete(String uid);

    void save(String token, Long uid);
}
