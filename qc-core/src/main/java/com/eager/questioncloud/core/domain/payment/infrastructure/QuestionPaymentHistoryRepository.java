package com.eager.questioncloud.core.domain.payment.infrastructure;

import com.eager.questioncloud.core.common.PagingInformation;
import com.eager.questioncloud.core.domain.payment.model.QuestionPaymentHistory;
import java.util.List;

public interface QuestionPaymentHistoryRepository {
    List<QuestionPaymentHistory> getQuestionPaymentHistory(Long userId, PagingInformation pagingInformation);

    int count(Long userId);

    QuestionPaymentHistory save(QuestionPaymentHistory questionPaymentHistory);
}
