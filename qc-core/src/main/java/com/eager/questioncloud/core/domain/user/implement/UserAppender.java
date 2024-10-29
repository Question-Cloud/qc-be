package com.eager.questioncloud.core.domain.user.implement;

import com.eager.questioncloud.core.domain.user.model.User;
import com.eager.questioncloud.core.domain.user.repository.UserRepository;
import com.eager.questioncloud.core.exception.CustomException;
import com.eager.questioncloud.core.exception.Error;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserAppender {
    private final UserRepository userRepository;

    public User create(User user) {
        if (checkDuplicateEmail(user.getUserInformation().getEmail())) {
            throw new CustomException(Error.DUPLICATE_EMAIL);
        }
        if (checkDuplicatePhone(user.getUserInformation().getPhone())) {
            throw new CustomException(Error.DUPLICATE_PHONE);
        }
        return userRepository.save(user);
    }

    public Boolean checkDuplicatePhone(String phone) {
        return userRepository.checkDuplicatePhone(phone);
    }

    public Boolean checkDuplicateEmail(String email) {
        return userRepository.checkDuplicateEmail(email);
    }
}
