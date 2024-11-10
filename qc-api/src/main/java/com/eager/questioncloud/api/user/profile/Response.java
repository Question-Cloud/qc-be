package com.eager.questioncloud.api.user.profile;

import com.eager.questioncloud.core.domain.user.dto.MyInformation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class Response {
    @Getter
    @Setter
    @AllArgsConstructor
    public static class MyInformationResponse {
        private MyInformation myInformation;
    }
}
