package com.eager.questioncloud.help;

import com.eager.questioncloud.user.UserReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HelpAccountService {
    private final UserReader userReader;

    public String recoverForgottenEmail(String phone) {
        return userReader.getUserByPhone(phone).getEmail();
    }
}
