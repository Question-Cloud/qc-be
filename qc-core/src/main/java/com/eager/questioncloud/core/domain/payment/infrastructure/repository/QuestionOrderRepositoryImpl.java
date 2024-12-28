package com.eager.questioncloud.core.domain.payment.infrastructure.repository;

import static com.eager.questioncloud.core.domain.payment.infrastructure.entity.QQuestionOrderEntity.questionOrderEntity;
import static com.eager.questioncloud.core.domain.payment.infrastructure.entity.QQuestionPaymentEntity.questionPaymentEntity;

import com.eager.questioncloud.core.domain.payment.infrastructure.entity.QuestionOrderEntity;
import com.eager.questioncloud.core.domain.payment.model.QuestionOrder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class QuestionOrderRepositoryImpl implements QuestionOrderRepository {
    private final QuestionOrderJpaRepository questionOrderJpaRepository;
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Boolean isAlreadyPurchased(Long userId, List<Long> questionIds) {
        return jpaQueryFactory.select(questionOrderEntity.id, questionPaymentEntity.userId)
            .from(questionOrderEntity)
            .leftJoin(questionPaymentEntity)
            .on(questionOrderEntity.orderId.eq(questionPaymentEntity.orderId), questionPaymentEntity.userId.eq(userId))
            .where(questionOrderEntity.questionId.in(questionIds))
            .fetchFirst() != null;
    }

    @Override
    public void save(QuestionOrder questionOrder) {
        questionOrderJpaRepository.saveAll(QuestionOrderEntity.from(questionOrder));
    }
}
