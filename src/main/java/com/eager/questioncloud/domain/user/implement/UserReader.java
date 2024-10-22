package com.eager.questioncloud.domain.user.implement;

import com.eager.questioncloud.domain.authentication.dto.AuthenticationDto.UserWithCreator;
import com.eager.questioncloud.domain.user.model.User;
import com.eager.questioncloud.domain.user.vo.AccountType;
import com.eager.questioncloud.domain.user.repository.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserReader {
    private final UserRepository userRepository;

    public Optional<User> getSocialUser(AccountType accountType, String socialUid) {
        return userRepository.getSocialUser(accountType, socialUid);
    }

    public User getUser(Long uid) {
        return userRepository.getUser(uid);
    }

    public UserWithCreator getUserWithCreator(Long uid) {
        return userRepository.getUserWithCreatorId(uid);
    }

    public User getUserByEmail(String email) {
        return userRepository.getUserByEmail(email);
    }

    public User getUserByPhone(String phone) {
        return userRepository.getUserByPhone(phone);
    }
}
