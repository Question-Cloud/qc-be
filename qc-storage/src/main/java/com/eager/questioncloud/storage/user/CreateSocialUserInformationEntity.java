package com.eager.questioncloud.storage.user;

import com.eager.questioncloud.core.domain.user.model.CreateSocialUserInformation;
import com.eager.questioncloud.core.domain.user.vo.AccountType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "create_social_user_information")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateSocialUserInformationEntity {
    @Id
    private String registerToken;

    @Column
    private String socialUid;

    @Column
    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    @Column
    private Boolean isUsed;

    @Builder
    public CreateSocialUserInformationEntity(String registerToken, String socialUid, AccountType accountType, Boolean isUsed) {
        this.registerToken = registerToken;
        this.socialUid = socialUid;
        this.accountType = accountType;
        this.isUsed = isUsed;
    }

    public CreateSocialUserInformation toModel() {
        return CreateSocialUserInformation.builder()
            .registerToken(registerToken)
            .socialUid(socialUid)
            .accountType(accountType)
            .isUsed(isUsed)
            .build();
    }

    public static CreateSocialUserInformationEntity from(CreateSocialUserInformation createSocialUserInformation) {
        return CreateSocialUserInformationEntity.builder()
            .registerToken(createSocialUserInformation.getRegisterToken())
            .socialUid(createSocialUserInformation.getSocialUid())
            .accountType(createSocialUserInformation.getAccountType())
            .isUsed(createSocialUserInformation.getIsUsed())
            .build();
    }
}