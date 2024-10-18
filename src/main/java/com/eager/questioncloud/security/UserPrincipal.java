package com.eager.questioncloud.security;

import com.eager.questioncloud.creator.model.Creator;
import com.eager.questioncloud.user.dto.UserDto.UserWithCreator;
import com.eager.questioncloud.user.model.User;
import java.io.Serializable;
import lombok.Getter;

@Getter
public class UserPrincipal extends org.springframework.security.core.userdetails.User implements Serializable {
    private final User user;
    private final Creator creator;

    public UserPrincipal(UserWithCreator userWithCreator) {
        super(userWithCreator.getUser().getUsername(), userWithCreator.getUser().getUsername(), userWithCreator.getUser().getAuthorities());
        this.user = userWithCreator.getUser();
        this.creator = userWithCreator.getCreator();
    }
}
