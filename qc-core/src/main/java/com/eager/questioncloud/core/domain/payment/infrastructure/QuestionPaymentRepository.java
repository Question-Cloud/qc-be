package com.eager.questioncloud.core.domain.payment.infrastructure;

import com.eager.questioncloud.core.common.PagingInformation;
import com.eager.questioncloud.core.domain.payment.dto.QuestionPaymentHistory;
import com.eager.questioncloud.core.domain.payment.model.QuestionPayment;
import java.util.List;

public interface QuestionPaymentRepository {
    QuestionPayment save(QuestionPayment questionPayment);

    List<QuestionPaymentHistory> getQuestionPaymentHistory(Long userId, PagingInformation pagingInformation);

    int countQuestionPaymentHistory(Long userId);

    QuestionPayment findByPaymentId(String paymentId);
}
