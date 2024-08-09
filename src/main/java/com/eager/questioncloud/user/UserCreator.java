package com.eager.questioncloud.user;

import com.eager.questioncloud.exception.CustomException;
import com.eager.questioncloud.exception.Error;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
public class UserCreator {
    private final UserRepository userRepository;

    public User create(User user) {
        if (checkDuplicateLoginId(user.getLoginId())) {
            throw new CustomException(Error.DUPLICATE_LOGIN_ID);
        }
        if (checkDuplicatePhone(user.getPhone())) {
            throw new CustomException(Error.DUPLICATE_PHONE);
        }
        if (checkDuplicateEmail(user.getEmail())) {
            throw new CustomException(Error.DUPLICATE_EMAIL);
        }
        return userRepository.append(user);
    }

    public Boolean checkDuplicateLoginId(String loginId) {
        if (StringUtils.hasText(loginId)) {
            return false;
        }
        return userRepository.checkDuplicateLoginId(loginId);
    }

    public Boolean checkDuplicatePhone(String phone) {
        return userRepository.checkDuplicatePhone(phone);
    }

    public Boolean checkDuplicateEmail(String email) {
        return userRepository.checkDuplicateEmail(email);
    }
}
