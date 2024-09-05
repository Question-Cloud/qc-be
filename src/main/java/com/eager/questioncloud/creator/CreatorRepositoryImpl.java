package com.eager.questioncloud.creator;

import static com.eager.questioncloud.creator.QCreatorEntity.creatorEntity;
import static com.eager.questioncloud.library.QUserQuestionLibraryEntity.userQuestionLibraryEntity;
import static com.eager.questioncloud.question.QQuestionEntity.questionEntity;
import static com.eager.questioncloud.question.QQuestionReviewEntity.questionReviewEntity;
import static com.eager.questioncloud.user.QUserEntity.userEntity;

import com.eager.questioncloud.creator.CreatorDto.CreatorInformation;
import com.eager.questioncloud.exception.CustomException;
import com.eager.questioncloud.exception.Error;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.MathExpressions;
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
    public Boolean existsById(Long creatorId) {
        return creatorJpaRepository.existsById(creatorId);
    }

    @Override
    public CreatorInformation getCreatorInformation(Long creatorId) {
        Integer salesCount = getCreatorSalesCount(creatorId);
        Double rate = getCreatorRate(creatorId);
        Tuple result = jpaQueryFactory.select(
                creatorEntity.id,
                userEntity.name,
                userEntity.profileImage,
                creatorEntity.mainSubject,
                userEntity.email,
//                subscribeCount,
                creatorEntity.introduction)
            .from(creatorEntity)
            .where(creatorEntity.id.eq(creatorId))
            .leftJoin(userEntity).on(userEntity.uid.eq(creatorEntity.userId))
            .fetchFirst();

        if (result == null) {
            throw new CustomException(Error.NOT_FOUND);

        }

        return CreatorInformation.builder()
            .creatorId(result.get(creatorEntity.id))
            .name(result.get(userEntity.name))
            .profileImage(result.get(userEntity.profileImage))
            .mainSubject(result.get(creatorEntity.mainSubject))
            .email(result.get(userEntity.email))
            .salesCount(salesCount)
            .rate(rate)
            .introduction(result.get(creatorEntity.introduction))
            .build();
    }

    @Override
    public Creator findByUserId(Long userId) {
        return creatorJpaRepository.findByUserId(userId)
            .orElseThrow(() -> new CustomException(Error.NOT_FOUND))
            .toModel();
    }

    @Override
    public Creator save(Creator creator) {
        return creatorJpaRepository.save(creator.toEntity()).toModel();
    }

    private Integer getCreatorSalesCount(Long creatorId) {
        return jpaQueryFactory
            .select(userQuestionLibraryEntity.id.countDistinct().intValue())
            .from(questionEntity)
            .leftJoin(userQuestionLibraryEntity).on(userQuestionLibraryEntity.questionId.eq(questionEntity.id))
            .where(questionEntity.creatorId.eq(creatorId))
            .fetchFirst();
    }

    private Double getCreatorRate(Long creatorId) {
        return jpaQueryFactory.select(
                MathExpressions.round(questionReviewEntity.rate.avg(), 1).coalesce(0.0))
            .from(questionEntity)
            .leftJoin(questionReviewEntity).on(questionReviewEntity.questionId.eq(questionEntity.id))
            .where(questionEntity.creatorId.eq(creatorId))
            .fetchFirst();
    }
}
