package com.eager.questioncloud.authentication;

import com.eager.questioncloud.exception.CustomException;
import com.eager.questioncloud.exception.Error;
import com.eager.questioncloud.security.UserPrincipal;
import com.eager.questioncloud.user.AccountType;
import com.eager.questioncloud.user.User;
import com.eager.questioncloud.user.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthenticationManager {
    private final UserRepository userRepository;

    public User getUserByCredentials(String email, String password) {
        User user = userRepository.getUserByEmail(email);
        if (!PasswordProcessor.matches(password, user.getPassword())) {
            throw new CustomException(Error.FAIL_LOGIN);
        }
        user.checkUserStatus();
        return user;
    }

    public Optional<User> getUserBySocialUid(AccountType accountType, String socialUid) {
        return userRepository.getSocialUser(accountType, socialUid);
    }

    public void authentication(Long uid) {
        User user = userRepository.getUser(uid);
        UserPrincipal userPrincipal = new UserPrincipal(user);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
            userPrincipal,
            user.getPassword(),
            user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
    }
}
