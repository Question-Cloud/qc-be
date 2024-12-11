package com.eager.questioncloud.application.api.user.profile;

import com.eager.questioncloud.core.domain.user.dto.MyInformation;
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
