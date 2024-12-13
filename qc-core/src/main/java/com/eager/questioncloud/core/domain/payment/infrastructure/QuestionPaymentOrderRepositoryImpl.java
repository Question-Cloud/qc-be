package com.eager.questioncloud.core.domain.payment.infrastructure;

import static com.eager.questioncloud.core.domain.payment.infrastructure.QQuestionPaymentEntity.questionPaymentEntity;
import static com.eager.questioncloud.core.domain.payment.infrastructure.QQuestionPaymentOrderEntity.questionPaymentOrderEntity;

import com.eager.questioncloud.core.domain.payment.model.QuestionPaymentOrder;
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
            .on(questionPaymentOrderEntity.orderId.eq(questionPaymentEntity.orderId), questionPaymentEntity.userId.eq(userId))
            .where(questionPaymentOrderEntity.questionId.in(questionIds))
            .fetchFirst() != null;
    }

    @Override
    public void save(QuestionPaymentOrder questionPaymentOrder) {
        questionPaymentOrderJpaRepository.saveAll(QuestionPaymentOrderEntity.from(questionPaymentOrder));
    }
}
