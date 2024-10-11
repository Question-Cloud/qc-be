package com.eager.questioncloud.user.dto;

import com.eager.questioncloud.creator.domain.Creator;
import com.eager.questioncloud.user.domain.User;
import com.eager.questioncloud.user.vo.UserType;
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

    @Getter
    @AllArgsConstructor
    public static class UserWithCreator {
        private User user;
        private Creator creator;
    }
}
