package com.eager.questioncloud.security;

import com.eager.questioncloud.core.domain.creator.model.Creator;
import com.eager.questioncloud.core.domain.user.model.User;
import com.eager.questioncloud.core.domain.user.vo.UserType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Getter
public class UserPrincipal implements Serializable {
    private final User user;
    private final Creator creator;

    private UserPrincipal(User user, Creator creator) {
        this.user = user;
        this.creator = creator;
    }

    public static UserPrincipal create(User user, Creator creator) {
        return new UserPrincipal(user, creator);
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        if (user.getUid().equals(-1L)) {
            authorities.add(new SimpleGrantedAuthority("ROLE_GUEST"));
        } else if (user.getUserType().equals(UserType.NormalUser)) {
            authorities.add(new SimpleGrantedAuthority("ROLE_NormalUser"));
        } else if (user.getUserType().equals(UserType.CreatorUser)) {
            authorities.add(new SimpleGrantedAuthority("ROLE_CreatorUser"));
        }
        return authorities;
    }
}
