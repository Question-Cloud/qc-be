package com.eager.questioncloud.core.domain.payment.infrastructure.repository;

import com.eager.questioncloud.core.common.PagingInformation;
import com.eager.questioncloud.core.domain.payment.infrastructure.document.QuestionPaymentHistoryDocument;
import com.eager.questioncloud.core.domain.payment.model.QuestionPaymentHistory;
import com.eager.questioncloud.core.exception.CoreException;
import com.eager.questioncloud.core.exception.Error;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class QuestionPaymentHistoryRepositoryImpl implements QuestionPaymentHistoryRepository {
    private final QuestionPaymentHistoryMongoRepository questionPaymentHistoryMongoRepository;

    @Override
    public List<QuestionPaymentHistory> getQuestionPaymentHistory(Long userId, PagingInformation pagingInformation) {
        return questionPaymentHistoryMongoRepository.findByUserId(
                userId,
                PageRequest.of(pagingInformation.getPage(), pagingInformation.getSize(), Sort.by(Sort.Order.desc("paymentId")))
            )
            .stream()
            .map(QuestionPaymentHistoryDocument::toModel)
            .collect(Collectors.toList());
    }

    @Override
    public int count(Long userId) {
        return questionPaymentHistoryMongoRepository.countByUserId(userId);
    }

    @Override
    public QuestionPaymentHistory save(QuestionPaymentHistory questionPaymentHistory) {
        return questionPaymentHistoryMongoRepository.save(QuestionPaymentHistoryDocument.from(questionPaymentHistory)).toModel();
    }

    @Override
    public QuestionPaymentHistory findById(Long id) {
        return questionPaymentHistoryMongoRepository.findById(id)
            .orElseThrow(() -> new CoreException(Error.NOT_FOUND))
            .toModel();
    }
}
