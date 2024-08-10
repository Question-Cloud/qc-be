package com.eager.questioncloud.authentication;

public interface RefreshTokenRepository {
    String get(Long uid);

    void delete(String uid);

    void save(String token, Long uid);
}
