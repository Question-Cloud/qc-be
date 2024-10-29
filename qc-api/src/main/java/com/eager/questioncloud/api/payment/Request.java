package com.eager.questioncloud.api.payment;

import com.eager.questioncloud.core.exception.CustomException;
import com.eager.questioncloud.core.exception.Error;
import com.eager.questioncloud.validator.Validatable;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

public class Request {
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
