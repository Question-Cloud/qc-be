package com.eager.questioncloud.user.implement;

import com.eager.questioncloud.exception.CustomException;
import com.eager.questioncloud.exception.Error;
import com.eager.questioncloud.user.model.User;
import com.eager.questioncloud.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserAppender {
    private final UserRepository userRepository;

    public User create(User user) {
        if (checkDuplicateEmail(user.getEmail())) {
            throw new CustomException(Error.DUPLICATE_EMAIL);
        }
        if (checkDuplicatePhone(user.getPhone())) {
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
