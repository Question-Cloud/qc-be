package com.eager.questioncloud.core.domain.user.dto;

import com.eager.questioncloud.core.domain.creator.model.Creator;
import com.eager.questioncloud.core.domain.user.model.User;
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
