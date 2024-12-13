package com.eager.questioncloud.core.domain.payment.infrastructure;

import static com.eager.questioncloud.core.domain.coupon.infrastructure.QCouponEntity.couponEntity;
import static com.eager.questioncloud.core.domain.coupon.infrastructure.QUserCouponEntity.userCouponEntity;
import static com.eager.questioncloud.core.domain.creator.infrastructure.QCreatorEntity.creatorEntity;
import static com.eager.questioncloud.core.domain.payment.infrastructure.QQuestionPaymentEntity.questionPaymentEntity;
import static com.eager.questioncloud.core.domain.payment.infrastructure.QQuestionPaymentOrderEntity.questionPaymentOrderEntity;
import static com.eager.questioncloud.core.domain.question.infrastructure.QQuestionEntity.questionEntity;
import static com.eager.questioncloud.core.domain.user.infrastructure.QUserEntity.userEntity;

import com.eager.questioncloud.core.common.PagingInformation;
import com.eager.questioncloud.core.domain.payment.dto.QuestionPaymentHistory;
import com.eager.questioncloud.core.domain.payment.model.QuestionPayment;
import com.eager.questioncloud.core.domain.question.infrastructure.QQuestionCategoryEntity;
import com.eager.questioncloud.exception.CustomException;
import com.eager.questioncloud.exception.Error;
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
    private final QQuestionCategoryEntity parent = new QQuestionCategoryEntity("parent");
    private final QQuestionCategoryEntity child = new QQuestionCategoryEntity("child");

    @Override
    public QuestionPayment save(QuestionPayment questionPayment) {
        return questionPaymentJpaRepository.save(QuestionPaymentEntity.from(questionPayment)).toModel();
    }

    @Override
    public List<QuestionPaymentHistory> getQuestionPaymentHistory(Long userId, PagingInformation pagingInformation) {
        Map<String, QuestionPaymentHistory> resultMap = jpaQueryFactory
            .from(questionPaymentEntity)
            .offset(pagingInformation.getOffset())
            .limit(pagingInformation.getSize())
            .leftJoin(questionPaymentOrderEntity).on(questionPaymentOrderEntity.orderId.eq(questionPaymentEntity.orderId))
            .leftJoin(questionEntity).on(questionEntity.id.eq(questionPaymentOrderEntity.questionId))
            .leftJoin(creatorEntity).on(creatorEntity.id.eq(questionEntity.creatorId))
            .leftJoin(userEntity).on(userEntity.uid.eq(creatorEntity.userId))
            .leftJoin(userCouponEntity).on(userCouponEntity.id.eq(questionPaymentEntity.userCouponId))
            .leftJoin(couponEntity).on(couponEntity.id.eq(userCouponEntity.couponId))
            .leftJoin(child).on(child.id.eq(questionEntity.questionContentEntity.questionCategoryId))
            .leftJoin(parent).on(parent.id.eq(child.parentId))
            .transform(GroupBy.groupBy(questionPaymentEntity.orderId).as(
                Projections.constructor(
                    QuestionPaymentHistory.class,
                    questionPaymentEntity.orderId,
                    GroupBy.list(
                        Projections.constructor(
                            QuestionPaymentHistory.OrderQuestion.class,
                            questionEntity.id,
                            questionPaymentOrderEntity.price,
                            questionEntity.questionContentEntity.title,
                            questionEntity.questionContentEntity.thumbnail,
                            userEntity.userInformationEntity.name,
                            questionEntity.questionContentEntity.subject,
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

    @Override
    public QuestionPayment findByPaymentId(String paymentId) {
        return questionPaymentJpaRepository.findByOrderId(paymentId)
            .orElseThrow(() -> new CustomException(Error.NOT_FOUND))
            .toModel();
    }
}
