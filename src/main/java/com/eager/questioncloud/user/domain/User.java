package com.eager.questioncloud.user.domain;

import com.eager.questioncloud.authentication.implement.PasswordProcessor;
import com.eager.questioncloud.exception.CustomException;
import com.eager.questioncloud.exception.Error;
import com.eager.questioncloud.exception.NotVerificationUserException;
import com.eager.questioncloud.user.entity.UserEntity;
import com.eager.questioncloud.user.vo.AccountType;
import com.eager.questioncloud.user.vo.UserStatus;
import com.eager.questioncloud.user.vo.UserType;
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
    private String email;
    private String password;
    private String socialUid;
    private AccountType accountType;
    private String phone;
    private String name;
    private String profileImage;
    private int point;
    private UserType userType;
    private UserStatus userStatus;

    @Builder
    public User(Long uid, String email, String password, String socialUid, AccountType accountType, String phone, String name, String profileImage,
        int point, UserType userType, UserStatus userStatus) {
        this.uid = uid;
        this.email = email;
        this.password = password;
        this.socialUid = socialUid;
        this.accountType = accountType;
        this.phone = phone;
        this.name = name;
        this.profileImage = profileImage;
        this.point = point;
        this.userType = userType;
        this.userStatus = userStatus;
    }

    public static User guest() {
        return User.builder()
            .uid(-1L)
            .email("guest")
            .password("guest")
            .name("guest")
            .build();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        if (uid.equals(-1L)) {
            authorities.add(new SimpleGrantedAuthority("ROLE_GUEST"));
        } else if (userType.equals(UserType.NormalUser)) {
            authorities.add(new SimpleGrantedAuthority("ROLE_NormalUser"));
        } else if (userType.equals(UserType.CreatorUser)) {
            authorities.add(new SimpleGrantedAuthority("ROLE_CreatorUser"));
        }
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
            throw new NotVerificationUserException(this);
        }
        if (!userStatus.equals(UserStatus.Active)) {
            throw new CustomException(Error.NOT_ACTIVE_USER);
        }
    }

    public void usePoint(int amount) {
        if (point < amount) {
            throw new CustomException(Error.NOT_ENOUGH_POINT);
        }
        this.point = this.point - amount;
    }

    public void changePassword(String newPassword) {
        if (!accountType.equals(AccountType.EMAIL)) {
            throw new CustomException(Error.NOT_PASSWORD_SUPPORT_ACCOUNT);
        }
        this.password = PasswordProcessor.encode(newPassword);
    }

    public User update(String name, String profileImage) {
        this.name = name;
        this.profileImage = profileImage;
        return this;
    }

    public static User create(CreateUser createUser) {
        return User.builder()
            .email(createUser.getEmail())
            .password(PasswordProcessor.encode(createUser.getPassword()))
            .accountType(AccountType.EMAIL)
            .phone(createUser.getPhone())
            .name(createUser.getName())
            .point(0)
            .userType(UserType.NormalUser)
            .userStatus(UserStatus.PendingEmailVerification)
            .build();
    }

    public static User create(CreateUser createUser, String socialUid) {
        return User.builder()
            .email(createUser.getEmail())
            .socialUid(socialUid)
            .accountType(createUser.getAccountType())
            .phone(createUser.getPhone())
            .name(createUser.getName())
            .point(0)
            .userType(UserType.NormalUser)
            .userStatus(UserStatus.PendingEmailVerification)
            .build();
    }

    public UserEntity toEntity() {
        return UserEntity.builder()
            .uid(uid)
            .email(email)
            .password(password)
            .socialUid(socialUid)
            .accountType(accountType)
            .phone(phone)
            .name(name)
            .profileImage(profileImage)
            .point(point)
            .userType(userType)
            .userStatus(userStatus)
            .build();
    }
}
