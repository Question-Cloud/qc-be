package com.eager.questioncloud.core.domain.user.infrastructure;

import com.eager.questioncloud.core.domain.user.model.UserInformation;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class UserInformationEntity {
    private String email;
    private String phone;
    private String name;
    private String profileImage;

    public static UserInformationEntity from(UserInformation userInformation) {
        return new UserInformationEntity(
            userInformation.getEmail(),
            userInformation.getPhone(),
            userInformation.getName(),
            userInformation.getProfileImage());
    }

    public UserInformation toModel() {
        return new UserInformation(email, phone, name, profileImage);
    }
}
