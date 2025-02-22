package com.eager.questioncloud.core.domain.review.infrastructure.repository;

import static com.eager.questioncloud.core.domain.review.infrastructure.entity.QQuestionReviewEntity.questionReviewEntity;
import static com.eager.questioncloud.core.domain.user.infrastructure.entity.QUserEntity.userEntity;

import com.eager.questioncloud.core.common.PagingInformation;
import com.eager.questioncloud.core.domain.review.dto.QuestionReviewDetail;
import com.eager.questioncloud.core.domain.review.infrastructure.entity.QQuestionReviewEntity;
import com.eager.questioncloud.core.domain.review.infrastructure.entity.QuestionReviewEntity;
import com.eager.questioncloud.core.domain.review.model.QuestionReview;
import com.eager.questioncloud.core.domain.user.enums.UserType;
import com.eager.questioncloud.core.exception.CoreException;
import com.eager.questioncloud.core.exception.Error;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.MathExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class QuestionReviewRepositoryImpl implements QuestionReviewRepository {
    private final JPAQueryFactory jpaQueryFactory;
    private final QuestionReviewJpaRepository questionReviewJpaRepository;

    @Override
    public int getTotal(Long questionId) {
        Integer total = jpaQueryFactory.select(questionReviewEntity.id.count().intValue())
            .from(questionReviewEntity)
            .where(questionReviewEntity.questionId.eq(questionId), questionReviewEntity.isDeleted.isFalse())
            .fetchFirst();

        if (total == null) {
            return 0;
        }
        return total;
    }

    @Override
    public List<QuestionReviewDetail> getQuestionReviews(Long questionId, Long userId, PagingInformation pagingInformation) {
        QQuestionReviewEntity profile = new QQuestionReviewEntity("profile");
        List<Tuple> result = jpaQueryFactory.select(
                questionReviewEntity.id,
                userEntity.userInformationEntity.name,
                userEntity.uid,
                userEntity.userType,
                profile.id.count().intValue(),
                MathExpressions.round(profile.rate.avg(), 1),
                questionReviewEntity.rate,
                questionReviewEntity.comment,
                questionReviewEntity.createdAt)
            .from(questionReviewEntity)
            .where(questionReviewEntity.questionId.eq(questionId), questionReviewEntity.isDeleted.isFalse())
            .leftJoin(profile).on(profile.reviewerId.eq(questionReviewEntity.reviewerId), profile.isDeleted.isFalse())
            .leftJoin(userEntity).on(userEntity.uid.eq(questionReviewEntity.reviewerId))
            .groupBy(questionReviewEntity.id)
            .offset(pagingInformation.getOffset())
            .limit(pagingInformation.getSize())
            .fetch();

        return result
            .stream()
            .map(tuple -> new QuestionReviewDetail(
                tuple.get(questionReviewEntity.id),
                tuple.get(userEntity.userInformationEntity.name),
                UserType.CreatorUser.equals(tuple.get(userEntity.userType)),
                userId.equals(tuple.get(userEntity.uid)),
                tuple.get(profile.id.count().intValue()),
                tuple.get(MathExpressions.round(profile.rate.avg(), 1)),
                tuple.get(questionReviewEntity.rate),
                tuple.get(questionReviewEntity.comment),
                tuple.get(questionReviewEntity.createdAt)
            ))
            .collect(Collectors.toList());
    }

    @Override
    public QuestionReview getMyQuestionReview(Long questionId, Long userId) {
        return questionReviewJpaRepository.findByQuestionIdAndReviewerIdAndIsDeletedFalse(questionId, userId)
            .orElseThrow(() -> new CoreException(Error.NOT_FOUND))
            .toModel();
    }

    @Override
    public QuestionReview findByIdAndUserId(Long reviewId, Long userId) {
        QuestionReviewEntity result = jpaQueryFactory.select(questionReviewEntity)
            .from(questionReviewEntity)
            .where(questionReviewEntity.id.eq(reviewId), questionReviewEntity.reviewerId.eq(userId), questionReviewEntity.isDeleted.isFalse())
            .fetchFirst();

        if (result == null) {
            throw new CoreException(Error.NOT_FOUND);
        }

        return result.toModel();
    }

    @Override
    public Boolean isWritten(Long userId, Long questionId) {
        Long reviewId = jpaQueryFactory.select(questionReviewEntity.id)
            .from(questionReviewEntity)
            .where(
                questionReviewEntity.questionId.eq(questionId),
                questionReviewEntity.reviewerId.eq(userId),
                questionReviewEntity.isDeleted.isFalse())
            .fetchFirst();

        return reviewId != null;
    }

    @Override
    public QuestionReview save(QuestionReview questionReview) {
        return questionReviewJpaRepository.save(QuestionReviewEntity.from(questionReview)).toModel();
    }
}
