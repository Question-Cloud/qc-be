package com.eager.questioncloud.core.domain.payment;

import com.eager.questioncloud.core.common.PagingInformation;
import java.util.List;

public interface QuestionPaymentRepository {
    QuestionPayment save(QuestionPayment questionPayment);

    List<QuestionPaymentHistory> getQuestionPaymentHistory(Long userId, PagingInformation pagingInformation);

    int countQuestionPaymentHistory(Long userId);

    QuestionPayment findByPaymentId(String paymentId);
}
