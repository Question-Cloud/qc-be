package com.eager.questioncloud.core.domain.user.model;

import com.eager.questioncloud.core.domain.user.enums.AccountType;
import com.eager.questioncloud.core.domain.user.implement.PasswordProcessor;
import com.eager.questioncloud.core.exception.CoreException;
import com.eager.questioncloud.core.exception.Error;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserAccountInformation {
    private String password;
    private String socialUid;
    private AccountType accountType;
    public static UserAccountInformation guest = new UserAccountInformation("guest", "guest", AccountType.GUEST);

    @Builder
    public UserAccountInformation(String password, String socialUid, AccountType accountType) {
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
            throw new CoreException(Error.NOT_PASSWORD_SUPPORT_ACCOUNT);
        }
        String newEncodedPassword = PasswordProcessor.encode(newRawPassword);
        return new UserAccountInformation(newEncodedPassword, this.socialUid, this.accountType);
    }
}
