package com.eager.questioncloud.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class UserDto {
    @Getter
    @AllArgsConstructor
    public static class MyInformation {
        private String profileImage;
        private String name;
        private String email;
        private String phone;
        private UserType userType;

        public static MyInformation of(User user) {
            return new MyInformation(user.getProfileImage(), user.getName(), user.getEmail(), user.getPhone(), user.getUserType());
        }
    }
}
