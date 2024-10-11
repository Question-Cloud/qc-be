package com.eager.questioncloud.user.model;

import com.eager.questioncloud.authentication.implement.PasswordProcessor;
import com.eager.questioncloud.exception.CustomException;
import com.eager.questioncloud.exception.Error;
import com.eager.questioncloud.user.vo.AccountType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

    public UserAccountInformation changePassword(String newRawPassword) {
        if (!accountType.equals(AccountType.EMAIL)) {
            throw new CustomException(Error.NOT_PASSWORD_SUPPORT_ACCOUNT);
        }
        String newEncodedPassword = PasswordProcessor.encode(newRawPassword);
        return new UserAccountInformation(newEncodedPassword, this.socialUid, this.accountType);
    }

    public UserAccountInformation(String rawPassword, String socialUid, AccountType accountType) {
        this.password = PasswordProcessor.encode(rawPassword);
        this.socialUid = socialUid;
        this.accountType = accountType;
    }
}
