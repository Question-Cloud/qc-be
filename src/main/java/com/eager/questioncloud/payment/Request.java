package com.eager.questioncloud.payment;

import java.util.List;
import lombok.Getter;

public class Request {
    @Getter
    public static class QuestionPaymentRequest {
        private List<Long> questionIds;
        private Long couponId;
    }
}
