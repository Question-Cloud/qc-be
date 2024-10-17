package com.eager.questioncloud.payment.repository;

import com.eager.questioncloud.payment.entity.QuestionPaymentOrderEntity;
import com.eager.questioncloud.payment.model.QuestionPaymentOrder;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class QuestionPaymentOrderRepositoryImpl implements QuestionPaymentOrderRepository {
    private final QuestionPaymentOrderJpaRepository questionPaymentOrderJpaRepository;

    @Override
    public List<QuestionPaymentOrder> saveAll(List<QuestionPaymentOrder> questionPaymentOrders) {
        return QuestionPaymentOrderEntity.toModel(questionPaymentOrderJpaRepository.saveAll(QuestionPaymentOrder.toEntity(questionPaymentOrders)));
    }
}
