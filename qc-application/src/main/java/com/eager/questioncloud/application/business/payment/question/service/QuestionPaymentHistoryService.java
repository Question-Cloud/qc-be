package com.eager.questioncloud.application.business.payment.question.service;

import com.eager.questioncloud.core.common.PagingInformation;
import com.eager.questioncloud.core.domain.payment.infrastructure.repository.QuestionPaymentHistoryRepository;
import com.eager.questioncloud.core.domain.payment.model.QuestionPaymentHistory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionPaymentHistoryService {
    private final QuestionPaymentHistoryRepository questionPaymentHistoryRepository;

    public List<QuestionPaymentHistory> getQuestionPaymentHistory(Long userId, PagingInformation pagingInformation) {
        return questionPaymentHistoryRepository.getQuestionPaymentHistory(userId, pagingInformation);
    }

    public int countQuestionPaymentHistory(Long userId) {
        return questionPaymentHistoryRepository.count(userId);
    }
}
