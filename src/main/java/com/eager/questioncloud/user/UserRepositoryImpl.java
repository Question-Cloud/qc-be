package com.eager.questioncloud.user;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private final UserJpaRepository userJpaRepository;

    @Override
    public User append(User user) {
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
    public Boolean checkDuplicateSocialUid(AccountType accountType, String socialUid) {
        return userJpaRepository.existsByAccountTypeAndSocialUid(accountType, socialUid);
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
