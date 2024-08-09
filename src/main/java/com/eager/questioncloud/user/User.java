package com.eager.questioncloud.user;

import com.eager.questioncloud.exception.CustomException;
import com.eager.questioncloud.exception.Error;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
public class User implements UserDetails {
    private Long uid;
    private String loginId;
    private String password;
    private String socialUid;
    private AccountType accountType;
    private String phone;
    private String name;
    private String email;
    private UserStatus userStatus;

    @Builder
    public User(Long uid, String loginId, String password, String socialUid, AccountType accountType, String phone, String name, String email,
        UserStatus userStatus) {
        this.uid = uid;
        this.loginId = loginId;
        this.password = password;
        this.socialUid = socialUid;
        this.accountType = accountType;
        this.phone = phone;
        this.name = name;
        this.email = email;
        this.userStatus = userStatus;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        return authorities;
    }

    @Override
    public String getUsername() {
        return this.name;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return !this.userStatus.equals(UserStatus.Active);
    }

    @Override
    public boolean isAccountNonLocked() {
        return !this.userStatus.equals(UserStatus.Active);
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !this.userStatus.equals(UserStatus.Active);
    }

    @Override
    public boolean isEnabled() {
        return !this.userStatus.equals(UserStatus.Active);
    }

    public void active() {
        this.userStatus = UserStatus.Active;
    }

    public void checkUserStatus() {
        if (userStatus.equals(UserStatus.PendingEmailVerification)) {
            throw new CustomException(Error.PENDING_EMAIL_VERIFICATION);
        }
        if (!userStatus.equals(UserStatus.Active)) {
            throw new CustomException(Error.NOT_ACTIVE_USER);
        }
    }

    public static User create(CreateUser createUser) {
        return User.builder()
            .loginId(createUser.getLoginId())
            .password(PasswordProcessor.encode(createUser.getPassword()))
            .accountType(createUser.getAccountType())
            .phone(createUser.getPhone())
            .name(createUser.getName())
            .email(createUser.getEmail())
            .userStatus(UserStatus.PendingEmailVerification)
            .build();
    }

    public static User create(CreateUser createUser, String socialUid) {
        return User.builder()
            .socialUid(socialUid)
            .accountType(createUser.getAccountType())
            .phone(createUser.getPhone())
            .name(createUser.getName())
            .email(createUser.getEmail())
            .userStatus(UserStatus.PendingEmailVerification)
            .build();
    }

    public UserEntity toEntity() {
        return UserEntity.builder()
            .uid(uid)
            .loginId(loginId)
            .password(password)
            .socialUid(socialUid)
            .accountType(accountType)
            .phone(phone)
            .name(name)
            .email(email)
            .userStatus(userStatus)
            .build();
    }
}
