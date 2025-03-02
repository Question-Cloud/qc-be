package com.eager.questioncloud.core.domain.payment.infrastructure.repository;

import com.eager.questioncloud.core.domain.payment.infrastructure.document.QuestionPaymentHistoryDocument;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface QuestionPaymentHistoryMongoRepository extends MongoRepository<QuestionPaymentHistoryDocument, Long> {
    List<QuestionPaymentHistoryDocument> findByUserId(Long userId, Pageable pageable);

    int countByUserId(Long userId);
}
