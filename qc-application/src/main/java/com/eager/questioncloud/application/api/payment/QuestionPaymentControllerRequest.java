package com.eager.questioncloud.application.api.payment;

import com.eager.questioncloud.application.validator.Validatable;
import com.eager.questioncloud.exception.CustomException;
import com.eager.questioncloud.exception.Error;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

public class QuestionPaymentControllerRequest {
    @Getter
    public static class QuestionPaymentRequest implements Validatable {
        private List<Long> questionIds = new ArrayList<>();
        private Long userCouponId;

        @Override
        public void validate() {
            if (questionIds == null || questionIds.isEmpty()) {
                throw new CustomException(Error.BAD_REQUEST);
            }
        }
    }
}
