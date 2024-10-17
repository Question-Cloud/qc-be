package com.eager.questioncloud.payment.dto;

import com.eager.questioncloud.exception.CustomException;
import com.eager.questioncloud.exception.Error;
import com.eager.questioncloud.valid.Validatable;
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
