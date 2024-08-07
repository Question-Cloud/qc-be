package com.eager.questioncloud.user;

public interface UserRepository {
    User append(User user);

    Boolean checkDuplicateLoginId(String loginId);

    Boolean checkDuplicateSocialUid(AccountType accountType, String socialUid);

    Boolean checkDuplicatePhone(String phone);

    Boolean checkDuplicateEmail(String email);
}
