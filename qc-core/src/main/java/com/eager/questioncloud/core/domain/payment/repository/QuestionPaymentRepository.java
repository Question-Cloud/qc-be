package com.eager.questioncloud.core.domain.payment.repository;

import com.eager.questioncloud.core.common.PagingInformation;
import com.eager.questioncloud.core.domain.payment.model.QuestionPayment;
import com.eager.questioncloud.core.domain.payment.model.QuestionPaymentHistory;
import java.util.List;

public interface QuestionPaymentRepository {
    QuestionPayment save(QuestionPayment questionPayment);

    List<QuestionPaymentHistory> getQuestionPaymentHistory(Long userId, PagingInformation pagingInformation);

    int countQuestionPaymentHistory(Long userId);
}
