package com.eager.questioncloud.user;

public interface RefreshTokenRepository {
    String get(String uid);

    void delete(String uid);

    void save(String token, Long uid);
}
