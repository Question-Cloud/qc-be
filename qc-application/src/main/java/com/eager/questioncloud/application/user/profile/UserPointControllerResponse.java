package com.eager.questioncloud.application.user.profile;

import com.eager.questioncloud.domain.user.MyInformation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class UserPointControllerResponse {
    @Getter
    @Setter
    @AllArgsConstructor
    public static class MyInformationResponse {
        private MyInformation myInformation;
    }
}
