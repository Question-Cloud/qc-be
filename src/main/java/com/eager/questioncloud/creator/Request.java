package com.eager.questioncloud.creator;

import com.eager.questioncloud.question.Subject;
import lombok.Getter;

public class Request {
    @Getter
    public static class RegisterCreatorRequest {
        private Subject mainSubject;
        private String introduction;
    }

    @Getter
    public static class UpdateMyCreatorInformationRequest {
        private Subject mainSubject;
        private String introduction;
    }
}
