package com.eager.questioncloud.domain.authentication.repository;

public interface RefreshTokenRepository {
    String getByUserId(Long uid);

    void delete(String uid);

    void save(String token, Long uid);
}
