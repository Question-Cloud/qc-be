package com.eager.questioncloud.core.domain.payment.service;

import com.eager.questioncloud.core.common.PagingInformation;
import com.eager.questioncloud.core.domain.payment.dto.QuestionPaymentHistory;
import com.eager.questioncloud.core.domain.payment.implement.QuestionPaymentHistoryReader;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionPaymentHistoryService {
    private final QuestionPaymentHistoryReader questionPaymentHistoryReader;

    public List<QuestionPaymentHistory> getQuestionPaymentHistory(Long userId, PagingInformation pagingInformation) {
        return questionPaymentHistoryReader.getQuestionPaymentHistory(userId, pagingInformation);
    }

    public int countQuestionPaymentHistory(Long userId) {
        return questionPaymentHistoryReader.countQuestionPaymentHistory(userId);
    }
}
