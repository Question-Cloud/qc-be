package com.eager.questioncloud.application.business.user.implement;

import com.eager.questioncloud.core.domain.point.implement.UserPointManager;
import com.eager.questioncloud.core.domain.user.dto.CreateUser;
import com.eager.questioncloud.core.domain.user.enums.AccountType;
import com.eager.questioncloud.core.domain.user.enums.UserStatus;
import com.eager.questioncloud.core.domain.user.enums.UserType;
import com.eager.questioncloud.core.domain.user.infrastructure.repository.UserRepository;
import com.eager.questioncloud.core.domain.user.model.User;
import com.eager.questioncloud.core.domain.user.model.UserAccountInformation;
import com.eager.questioncloud.core.domain.user.model.UserInformation;
import com.eager.questioncloud.social.SocialAPIManager;
import com.eager.questioncloud.social.SocialPlatform;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class UserRegister {
    private final UserRegistrationValidator userRegistrationValidator;
    private final UserPointManager userPointManager;
    private final SocialAPIManager socialAPIManager;
    private final UserRepository userRepository;

    @Transactional
    public User create(CreateUser createUser) {
        UserAccountInformation userAccountInformation = createUserAccountInformation(createUser);
        UserInformation userInformation = UserInformation.create(createUser);
        userRegistrationValidator.validate(userAccountInformation, userInformation);

        User user = userRepository.save(
            User.create(userAccountInformation, userInformation, UserType.NormalUser, UserStatus.PendingEmailVerification)
        );

        userPointManager.init(user.getUid());
        return user;
    }

    private UserAccountInformation createUserAccountInformation(CreateUser createUser) {
        if (createUser.getAccountType().equals(AccountType.EMAIL)) {
            return UserAccountInformation.createEmailAccountInformation(createUser.getPassword());
        }
        String socialUid = socialAPIManager.getSocialUid(
            createUser.getSocialRegisterToken(),
            SocialPlatform.valueOf(createUser.getAccountType().getValue())
        );
        return UserAccountInformation.createSocialAccountInformation(socialUid, createUser.getAccountType());
    }
}
