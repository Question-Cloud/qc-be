package com.eager.questioncloud.user.vo;

import com.eager.questioncloud.user.dto.Request.CreateUserRequest;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class UserInformation {
    private String email;

    private String phone;

    private String name;

    private String profileImage;

    public UserInformation updateUserInformation(String name, String profileImage) {
        return new UserInformation(this.email, this.phone, name, profileImage);
    }

    public static UserInformation from(CreateUserRequest createUserRequest) {
        return new UserInformation(createUserRequest.getEmail(), createUserRequest.getPhone(), createUserRequest.getName(), null);
    }
}
