package com.eager.questioncloud.security;

import com.eager.questioncloud.domain.creator.model.Creator;
import com.eager.questioncloud.domain.user.model.User;
import java.io.Serializable;
import lombok.Getter;

@Getter
public class UserPrincipal extends org.springframework.security.core.userdetails.User implements Serializable {
    private final User user;
    private final Creator creator;

    private UserPrincipal(User user, Creator creator) {
        super(user.getUsername(), user.getUsername(), user.getAuthorities());
        this.user = user;
        this.creator = creator;
    }

    public static UserPrincipal create(User user, Creator creator) {
        return new UserPrincipal(user, creator);
    }
}
