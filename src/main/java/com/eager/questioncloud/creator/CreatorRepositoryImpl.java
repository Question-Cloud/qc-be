package com.eager.questioncloud.creator;

import static com.eager.questioncloud.creator.QCreatorEntity.creatorEntity;
import static com.eager.questioncloud.library.QUserQuestionLibraryEntity.userQuestionLibraryEntity;
import static com.eager.questioncloud.question.QQuestionEntity.questionEntity;
import static com.eager.questioncloud.question.QQuestionReviewEntity.questionReviewEntity;
import static com.eager.questioncloud.user.QUserEntity.userEntity;

import com.eager.questioncloud.creator.CreatorDto.CreatorInformation;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.MathExpressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CreatorRepositoryImpl implements CreatorRepository {
    private final CreatorJpaRepository creatorJpaRepository;
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Creator append(Creator creator) {
        return creatorJpaRepository.save(creator.toEntity()).toModel();
    }

    @Override
    public Boolean existsByUserId(Long userId) {
        return creatorJpaRepository.existsByUserId(userId);
    }

    @Override
    public CreatorInformation getCreatorInformation(Long creatorId) {
        JPQLQuery<Integer> salesCount = JPAExpressions
            .select(userQuestionLibraryEntity.id.countDistinct().intValue())
            .from(questionEntity)
            .leftJoin(userQuestionLibraryEntity).on(userQuestionLibraryEntity.questionId.eq(questionEntity.id))
            .where(questionEntity.creatorId.eq(creatorId));

        JPQLQuery<Double> rate = JPAExpressions
            .select(MathExpressions.round(questionReviewEntity.rate.avg(), 1).coalesce(0.0))
            .from(questionEntity)
            .leftJoin(questionReviewEntity).on(questionReviewEntity.questionId.eq(questionEntity.id))
            .where(questionEntity.creatorId.eq(creatorId));

        return jpaQueryFactory.select(
                Projections.constructor(CreatorInformation.class,
                    creatorEntity.id,
                    userEntity.name,
                    userEntity.profileImage,
                    creatorEntity.mainSubject,
                    userEntity.email,
//                subscribeCount
                    salesCount,
                    rate,
                    creatorEntity.introduction
                ))
            .from(creatorEntity)
            .where(creatorEntity.id.eq(creatorId))
            .leftJoin(userEntity).on(userEntity.uid.eq(creatorEntity.userId))
            .fetchFirst();
    }
}
