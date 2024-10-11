package com.eager.questioncloud.user.vo;

import com.eager.questioncloud.authentication.implement.PasswordProcessor;
import com.eager.questioncloud.exception.CustomException;
import com.eager.questioncloud.exception.Error;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Embeddable
public class UserAccountInformation {
    private String password;

    private String socialUid;

    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    public static UserAccountInformation guest = new UserAccountInformation("guest", "guest", AccountType.GUEST);

    @Builder
    private UserAccountInformation(String password, String socialUid, AccountType accountType) {
        this.password = password;
        this.socialUid = socialUid;
        this.accountType = accountType;
    }

    public static UserAccountInformation createEmailAccountInformation(String rawPassword) {
        String encodedPassword = PasswordProcessor.encode(rawPassword);
        return UserAccountInformation.builder()
            .password(encodedPassword)
            .accountType(AccountType.EMAIL)
            .build();
    }

    public static UserAccountInformation createSocialAccountInformation(String socialUid, AccountType socialType) {
        return UserAccountInformation.builder()
            .socialUid(socialUid)
            .accountType(socialType)
            .build();
    }

    public static UserAccountInformation getGuestAccountInformation() {
        return guest;
    }

    public UserAccountInformation changePassword(String newRawPassword) {
        if (!accountType.equals(AccountType.EMAIL)) {
            throw new CustomException(Error.NOT_PASSWORD_SUPPORT_ACCOUNT);
        }
        String newEncodedPassword = PasswordProcessor.encode(newRawPassword);
        return new UserAccountInformation(newEncodedPassword, this.socialUid, this.accountType);
    }
}
