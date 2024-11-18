package com.eager.questioncloud.domain.user;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class UserAccountInformationEntity {
    private String password;

    private String socialUid;

    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    public static UserAccountInformationEntity from(UserAccountInformation userAccountInformation) {
        return new UserAccountInformationEntity(
            userAccountInformation.getPassword(),
            userAccountInformation.getSocialUid(),
            userAccountInformation.getAccountType());
    }

    public UserAccountInformation toModel() {
        return new UserAccountInformation(password, socialUid, accountType);
    }
}
