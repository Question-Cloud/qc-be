package com.eager.questioncloud.authentication.repository;

public interface RefreshTokenRepository {
    String getByUserId(Long uid);

    void delete(String uid);

    void save(String token, Long uid);
}
