package com.eager.questioncloud.user;

import com.eager.questioncloud.exception.CustomException;
import com.eager.questioncloud.exception.Error;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private final UserJpaRepository userJpaRepository;

    @Override
    public User getUserByLoginId(String loginId) {
        return userJpaRepository.findByLoginId(loginId)
            .orElseThrow(() -> new CustomException(Error.FAIL_LOGIN))
            .toDomain();
    }

    @Override
    public User getUser(Long uid) {
        return userJpaRepository.findById(uid)
            .orElseThrow(() -> new CustomException(Error.NOT_FOUND))
            .toDomain();
    }

    @Override
    public User append(User user) {
        return userJpaRepository.save(user.toEntity()).toDomain();
    }

    @Override
    public User save(User user) {
        return userJpaRepository.save(user.toEntity()).toDomain();
    }

    @Override
    public Optional<User> getSocialUser(AccountType accountType, String socialUid) {
        return userJpaRepository.findByAccountTypeAndSocialUid(accountType, socialUid);
    }

    @Override
    public Boolean checkDuplicateLoginId(String loginId) {
        return userJpaRepository.existsByLoginId(loginId);
    }

    @Override
    public Boolean checkDuplicatePhone(String phone) {
        return userJpaRepository.existsByPhone(phone);
    }

    @Override
    public Boolean checkDuplicateEmail(String email) {
        return userJpaRepository.existsByEmail(email);
    }
}
