package com.eager.questioncloud.user;

import java.util.Optional;

public interface UserRepository {
    User append(User user);

    Optional<User> getSocialUser(AccountType accountType, String socialUid);

    Boolean checkDuplicateLoginId(String loginId);

    Boolean checkDuplicateSocialUid(AccountType accountType, String socialUid);

    Boolean checkDuplicatePhone(String phone);

    Boolean checkDuplicateEmail(String email);
}
