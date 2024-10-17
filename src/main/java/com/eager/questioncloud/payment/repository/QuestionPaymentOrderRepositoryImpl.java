package com.eager.questioncloud.payment.repository;

import static com.eager.questioncloud.payment.entity.QQuestionPaymentEntity.questionPaymentEntity;
import static com.eager.questioncloud.payment.entity.QQuestionPaymentOrderEntity.questionPaymentOrderEntity;

import com.eager.questioncloud.payment.entity.QuestionPaymentOrderEntity;
import com.eager.questioncloud.payment.model.QuestionPaymentOrder;
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
            .on(questionPaymentOrderEntity.paymentId.eq(questionPaymentEntity.id), questionPaymentEntity.userId.eq(userId))
            .where(questionPaymentOrderEntity.questionId.in(questionIds))
            .fetchFirst() != null;
    }

    @Override
    public List<QuestionPaymentOrder> saveAll(List<QuestionPaymentOrder> questionPaymentOrders) {
        return QuestionPaymentOrderEntity.toModel(questionPaymentOrderJpaRepository.saveAll(QuestionPaymentOrder.toEntity(questionPaymentOrders)));
    }
}
