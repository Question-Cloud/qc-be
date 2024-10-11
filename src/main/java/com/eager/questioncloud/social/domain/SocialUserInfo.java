package com.eager.questioncloud.social.domain;

import com.eager.questioncloud.user.domain.AccountType;
import lombok.Getter;

@Getter
public class SocialUserInfo {
    private String uid;
    private String email;
    private String nickname;
    private AccountType accountType;

    public SocialUserInfo(String uid, String email, String nickname, AccountType accountType) {
        this.uid = uid;
        this.email = email;
        this.nickname = nickname;
        this.accountType = accountType;
    }
}
