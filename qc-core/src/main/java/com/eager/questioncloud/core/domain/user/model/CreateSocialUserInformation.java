package com.eager.questioncloud.core.domain.user.model;

import com.eager.questioncloud.core.domain.user.vo.AccountType;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CreateSocialUserInformation {
    private String registerToken;
    private String socialUid;
    private AccountType accountType;
    private Boolean isUsed;

    @Builder
    public CreateSocialUserInformation(String registerToken, String socialUid, AccountType accountType, Boolean isUsed) {
        this.registerToken = registerToken;
        this.socialUid = socialUid;
        this.accountType = accountType;
        this.isUsed = isUsed;
    }

    public static CreateSocialUserInformation create(AccountType accountType, String socialUid) {
        return CreateSocialUserInformation.builder()
            .registerToken(UUID.randomUUID().toString())
            .accountType(accountType)
            .socialUid(socialUid)
            .isUsed(false)
            .build();
    }

    public void use() {
        this.isUsed = true;
    }
}