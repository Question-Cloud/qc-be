package com.eager.questioncloud.application.api.payment;

import com.eager.questioncloud.core.common.PagingInformation;
import com.eager.questioncloud.core.domain.payment.QuestionPaymentHistory;
import com.eager.questioncloud.core.domain.payment.QuestionPaymentRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionPaymentHistoryService {
    private final QuestionPaymentRepository questionPaymentRepository;

    public List<QuestionPaymentHistory> getQuestionPaymentHistory(Long userId, PagingInformation pagingInformation) {
        return questionPaymentRepository.getQuestionPaymentHistory(userId, pagingInformation);
    }

    public int countQuestionPaymentHistory(Long userId) {
        return questionPaymentRepository.countQuestionPaymentHistory(userId);
    }
}
