package com.eager.questioncloud.core.domain.payment.implement;

import com.eager.questioncloud.core.common.PagingInformation;
import com.eager.questioncloud.core.domain.payment.dto.QuestionPaymentHistory;
import com.eager.questioncloud.core.domain.payment.repository.QuestionPaymentRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionPaymentHistoryReader {
    private final QuestionPaymentRepository questionPaymentRepository;

    public List<QuestionPaymentHistory> getQuestionPaymentHistory(Long userId, PagingInformation pagingInformation) {
        return questionPaymentRepository.getQuestionPaymentHistory(userId, pagingInformation);
    }

    public int countQuestionPaymentHistory(Long userId) {
        return questionPaymentRepository.countQuestionPaymentHistory(userId);
    }
}
