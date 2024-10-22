package com.eager.questioncloud.domain.user.dto;

import com.eager.questioncloud.domain.user.model.User;
import com.eager.questioncloud.domain.user.vo.UserType;
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

        public static MyInformation from(User user) {
            return new MyInformation(
                user.getUserInformation().getProfileImage(),
                user.getUserInformation().getName(),
                user.getUserInformation().getEmail(),
                user.getUserInformation().getPhone(),
                user.getUserType());
        }
    }
}
