package com.eager.questioncloud.domain.user.vo;

import com.eager.questioncloud.domain.user.dto.Request.CreateUserRequest;
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

    public static UserInformation guest = new UserInformation("guest", "guest", "guest", null);

    public static UserInformation create(CreateUserRequest createUserRequest) {
        return new UserInformation(createUserRequest.getEmail(), createUserRequest.getPhone(), createUserRequest.getName(), null);
    }

    public static UserInformation getGuestInformation() {
        return guest;
    }

    public UserInformation updateUserInformation(String name, String profileImage) {
        return new UserInformation(this.email, this.phone, name, profileImage);
    }
}
