package com.eager.questioncloud.core.domain.payment;

import static com.eager.questioncloud.core.domain.payment.QQuestionPaymentEntity.questionPaymentEntity;
import static com.eager.questioncloud.core.domain.payment.QQuestionPaymentOrderEntity.questionPaymentOrderEntity;

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
    public List<QuestionPaymentOrder> saveAll(List<QuestionPaymentOrder> questionPaymentOrders) {
        return QuestionPaymentOrderEntity.toModel(questionPaymentOrderJpaRepository.saveAll(QuestionPaymentOrderEntity.from(questionPaymentOrders)));
    }
}
