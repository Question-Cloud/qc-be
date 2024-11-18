package com.eager.questioncloud.domain.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserInformation {
    private String email;
    private String phone;
    private String name;
    private String profileImage;

    public static UserInformation guest = new UserInformation("guest", "guest", "guest", null);

    public static UserInformation create(CreateUser createUser) {
        return new UserInformation(createUser.getEmail(), createUser.getPhone(), createUser.getName(), null);
    }

    public static UserInformation getGuestInformation() {
        return guest;
    }

    public UserInformation updateUserInformation(String name, String profileImage) {
        return new UserInformation(this.email, this.phone, name, profileImage);
    }
}
