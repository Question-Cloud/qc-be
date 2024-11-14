package com.eager.questioncloud.storage.payment;

import static com.eager.questioncloud.storage.coupon.QCouponEntity.couponEntity;
import static com.eager.questioncloud.storage.coupon.QUserCouponEntity.userCouponEntity;
import static com.eager.questioncloud.storage.creator.QCreatorEntity.creatorEntity;
import static com.eager.questioncloud.storage.payment.QQuestionPaymentEntity.questionPaymentEntity;
import static com.eager.questioncloud.storage.payment.QQuestionPaymentOrderEntity.questionPaymentOrderEntity;
import static com.eager.questioncloud.storage.question.QQuestionEntity.questionEntity;
import static com.eager.questioncloud.storage.user.QUserEntity.userEntity;

import com.eager.questioncloud.core.common.PagingInformation;
import com.eager.questioncloud.core.domain.payment.model.QuestionPayment;
import com.eager.questioncloud.core.domain.payment.model.QuestionPaymentHistory;
import com.eager.questioncloud.core.domain.payment.repository.QuestionPaymentRepository;
import com.eager.questioncloud.storage.question.QQuestionCategoryEntity;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class QuestionPaymentRepositoryImpl implements QuestionPaymentRepository {
    private final QuestionPaymentJpaRepository questionPaymentJpaRepository;
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public QuestionPayment save(QuestionPayment questionPayment) {
        QuestionPaymentEntity result = questionPaymentJpaRepository.save(QuestionPaymentEntity.from(questionPayment));
        return QuestionPayment.builder()
            .id(result.getId())
            .userId(result.getUserId())
            .paymentId(result.getPaymentId())
            .orders(questionPayment.getOrders())
            .userCoupon(questionPayment.getUserCoupon())
            .amount(result.getAmount())
            .status(result.getStatus())
            .createdAt(result.getCreatedAt())
            .build();
    }

    @Override
    public List<QuestionPaymentHistory> getQuestionPaymentHistory(Long userId, PagingInformation pagingInformation) {
        QQuestionCategoryEntity parent = new QQuestionCategoryEntity("parent");
        QQuestionCategoryEntity child = new QQuestionCategoryEntity("child");
        Map<String, QuestionPaymentHistory> resultMap = jpaQueryFactory
            .from(questionPaymentEntity)
            .offset(pagingInformation.getOffset())
            .limit(pagingInformation.getSize())
            .leftJoin(questionPaymentOrderEntity).on(questionPaymentOrderEntity.paymentId.eq(questionPaymentEntity.paymentId))
            .leftJoin(questionEntity).on(questionEntity.id.eq(questionPaymentOrderEntity.questionId))
            .leftJoin(creatorEntity).on(creatorEntity.id.eq(questionEntity.creatorId))
            .leftJoin(userEntity).on(userEntity.uid.eq(creatorEntity.userId))
            .leftJoin(userCouponEntity).on(userCouponEntity.id.eq(questionPaymentEntity.userCouponId))
            .leftJoin(couponEntity).on(couponEntity.id.eq(userCouponEntity.couponId))
            .leftJoin(child).on(child.id.eq(questionEntity.questionCategoryId))
            .leftJoin(parent).on(parent.id.eq(child.parentId))
            .transform(GroupBy.groupBy(questionPaymentEntity.paymentId).as(
                Projections.constructor(
                    QuestionPaymentHistory.class,
                    questionPaymentEntity.paymentId,
                    GroupBy.list(
                        Projections.constructor(
                            QuestionPaymentHistory.OrderQuestion.class,
                            questionEntity.id,
                            questionPaymentOrderEntity.price,
                            questionEntity.questionContentEntity.title,
                            questionEntity.questionContentEntity.thumbnail,
                            userEntity.userInformationEntity.name,
                            questionEntity.subject,
                            parent.title,
                            child.title
                        )
                    ),
                    Projections.constructor(
                        QuestionPaymentHistory.QuestionPaymentCoupon.class,
                        couponEntity.title,
                        couponEntity.couponType,
                        couponEntity.value
                    ),
                    questionPaymentEntity.amount,
                    questionPaymentEntity.userCouponId.isNotNull(),
                    questionPaymentEntity.status,
                    questionPaymentEntity.createdAt
                )
            ));
        return new ArrayList<>(resultMap.values());
    }

    @Override
    public int countQuestionPaymentHistory(Long userId) {
        return jpaQueryFactory.select(questionPaymentEntity.id.count())
            .from(questionPaymentEntity)
            .where(questionPaymentEntity.userId.eq(userId))
            .fetchFirst()
            .intValue();
    }
}
