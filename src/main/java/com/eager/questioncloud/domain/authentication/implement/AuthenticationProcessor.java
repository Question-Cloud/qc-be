package com.eager.questioncloud.domain.authentication.implement;

import com.eager.questioncloud.domain.authentication.dto.AuthenticationDto.UserWithCreator;
import com.eager.questioncloud.domain.user.model.User;
import com.eager.questioncloud.domain.user.vo.AccountType;
import com.eager.questioncloud.exception.CustomException;
import com.eager.questioncloud.exception.Error;
import com.eager.questioncloud.security.UserPrincipal;
import com.eager.questioncloud.domain.user.implement.UserReader;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthenticationProcessor {
    private final UserReader userReader;

    public User getUserByCredentials(String email, String password) {
        User user = userReader.getUserByEmail(email);
        if (!PasswordProcessor.matches(password, user.getPassword())) {
            throw new CustomException(Error.FAIL_LOGIN);
        }
        user.checkUserStatus();
        return user;
    }

    public Optional<User> getUserBySocialUid(AccountType accountType, String socialUid) {
        return userReader.getSocialUser(accountType, socialUid);
    }

    public void authentication(Long uid) {
        UserWithCreator userWithCreator = userReader.getUserWithCreator(uid);
        UserPrincipal userPrincipal = UserPrincipal.create(userWithCreator.getUser(), userWithCreator.getCreator());
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
            userPrincipal,
            userWithCreator.getUser().getUsername(),
            userWithCreator.getUser().getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
    }

    public void setGuest() {
        User guest = User.guest();
        UserPrincipal userPrincipal = UserPrincipal.create(guest, null);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
            userPrincipal,
            guest.getPassword(),
            guest.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
    }
}
