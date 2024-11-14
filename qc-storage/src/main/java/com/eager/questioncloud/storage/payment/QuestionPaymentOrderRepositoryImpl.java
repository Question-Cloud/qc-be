package com.eager.questioncloud.storage.payment;

import static com.eager.questioncloud.storage.payment.QQuestionPaymentEntity.questionPaymentEntity;
import static com.eager.questioncloud.storage.payment.QQuestionPaymentOrderEntity.questionPaymentOrderEntity;

import com.eager.questioncloud.core.domain.payment.model.QuestionPaymentOrder;
import com.eager.questioncloud.core.domain.payment.repository.QuestionPaymentOrderRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class QuestionPaymentOrderRepositoryImpl implements QuestionPaymentOrderRepository {
    private final QuestionPaymentOrderJpaRepository questionPaymentOrderJpaRepository;
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Boolean isAlreadyPurchased(Long userId, List<Long> questionIds) {
        return jpaQueryFactory.select(questionPaymentOrderEntity.id, questionPaymentEntity.userId)
            .from(questionPaymentOrderEntity)
            .leftJoin(questionPaymentEntity)
            .on(questionPaymentOrderEntity.paymentId.eq(questionPaymentEntity.paymentId), questionPaymentEntity.userId.eq(userId))
            .where(questionPaymentOrderEntity.questionId.in(questionIds))
            .fetchFirst() != null;
    }

    @Override
    public void saveAll(List<QuestionPaymentOrder> questionPaymentOrders) {
        questionPaymentOrderJpaRepository.saveAll(QuestionPaymentOrderEntity.from(questionPaymentOrders));
    }
}
