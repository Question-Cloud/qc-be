package com.eager.questioncloud.user;

public interface RefreshTokenRepository {
    String get(Long uid);

    void delete(String uid);

    void save(String token, Long uid);
}
