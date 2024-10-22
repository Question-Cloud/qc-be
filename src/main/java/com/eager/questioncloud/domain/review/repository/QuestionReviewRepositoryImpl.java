package com.eager.questioncloud.domain.review.repository;

import static com.eager.questioncloud.review.entity.QQuestionReviewEntity.questionReviewEntity;
import static com.eager.questioncloud.user.entity.QUserEntity.userEntity;

import com.eager.questioncloud.domain.review.dto.QuestionReviewDto.QuestionReviewItem;
import com.eager.questioncloud.domain.review.model.QuestionReview;
import com.eager.questioncloud.domain.user.vo.UserType;
import com.eager.questioncloud.exception.CustomException;
import com.eager.questioncloud.exception.Error;
import com.eager.questioncloud.review.entity.QQuestionReviewEntity;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.MathExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
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
    public List<QuestionReviewItem> getQuestionReviews(Long questionId, Long userId, Pageable pageable) {
        QQuestionReviewEntity profile = new QQuestionReviewEntity("profile");
        List<Tuple> result = jpaQueryFactory.select(
                questionReviewEntity.id,
                userEntity.userInformation.name,
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
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        return result
            .stream()
            .map(tuple -> new QuestionReviewItem(
                tuple.get(questionReviewEntity.id),
                tuple.get(userEntity.userInformation.name),
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
            .orElseThrow(() -> new CustomException(Error.NOT_FOUND))
            .toModel();
    }

    @Override
    public QuestionReview getForModifyAndDelete(Long reviewId, Long userId) {
        return questionReviewJpaRepository.findByIdAndReviewerIdAndIsDeletedFalse(reviewId, userId)
            .stream()
            .filter(item -> !item.getIsDeleted())
            .findFirst()
            .orElseThrow(() -> new CustomException(Error.NOT_FOUND))
            .toModel();
    }

    @Override
    public Boolean isWritten(Long questionId, Long userId) {
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
        return questionReviewJpaRepository.save(questionReviewJpaRepository.save(questionReview.toEntity())).toModel();
    }
}
