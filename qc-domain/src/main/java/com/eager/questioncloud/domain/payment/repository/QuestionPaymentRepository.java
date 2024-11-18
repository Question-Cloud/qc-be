package com.eager.questioncloud.domain.payment.repository;

import com.eager.questioncloud.common.PagingInformation;
import com.eager.questioncloud.domain.payment.dto.QuestionPaymentHistory;
import com.eager.questioncloud.domain.payment.model.QuestionPayment;
import java.util.List;

public interface QuestionPaymentRepository {
    QuestionPayment save(QuestionPayment questionPayment);

    List<QuestionPaymentHistory> getQuestionPaymentHistory(Long userId, PagingInformation pagingInformation);

    int countQuestionPaymentHistory(Long userId);
}
