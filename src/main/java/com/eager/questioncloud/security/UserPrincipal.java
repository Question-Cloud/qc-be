package com.eager.questioncloud.security;

import com.eager.questioncloud.user.User;
import java.io.Serializable;
import lombok.Getter;

@Getter
public class UserPrincipal extends org.springframework.security.core.userdetails.User implements Serializable {
    private final User user;

    public UserPrincipal(User user) {
        super(user.getEmail(), user.getPassword(), user.getAuthorities());
        this.user = user;
    }
}
