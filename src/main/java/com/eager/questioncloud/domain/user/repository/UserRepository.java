package com.eager.questioncloud.domain.user.repository;

import com.eager.questioncloud.domain.authentication.dto.AuthenticationDto.UserWithCreator;
import com.eager.questioncloud.domain.user.model.User;
import com.eager.questioncloud.domain.user.vo.AccountType;
import java.util.Optional;

public interface UserRepository {
    User getUserByEmail(String email);

    User getUserByPhone(String phone);

    User getUser(Long uid);

    UserWithCreator getUserWithCreatorId(Long uid);

    User save(User user);

    Optional<User> getSocialUser(AccountType accountType, String socialUid);

    Boolean checkDuplicatePhone(String phone);

    Boolean checkDuplicateEmail(String email);
}
