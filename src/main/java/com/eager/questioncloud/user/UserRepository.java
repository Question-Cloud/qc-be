package com.eager.questioncloud.user;

import java.util.Optional;

public interface UserRepository {
    User getUserByEmail(String email);

    User getUser(Long uid);

    User append(User user);

    User save(User user);

    Optional<User> getSocialUser(AccountType accountType, String socialUid);

    Boolean checkDuplicatePhone(String phone);

    Boolean checkDuplicateEmail(String email);
}
