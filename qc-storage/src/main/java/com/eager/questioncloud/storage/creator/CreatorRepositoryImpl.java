package com.eager.questioncloud.storage.creator;

import static com.eager.questioncloud.storage.creator.QCreatorEntity.creatorEntity;
import static com.eager.questioncloud.storage.user.QUserEntity.userEntity;

import com.eager.questioncloud.core.domain.creator.dto.CreatorDto.CreatorInformation;
import com.eager.questioncloud.core.domain.creator.model.Creator;
import com.eager.questioncloud.core.domain.creator.repository.CreatorRepository;
import com.eager.questioncloud.exception.CustomException;
import com.eager.questioncloud.exception.Error;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CreatorRepositoryImpl implements CreatorRepository {
    private final CreatorJpaRepository creatorJpaRepository;
    private final JPAQueryFactory jpaQueryFactory;

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
                userEntity.userInformation.name,
                userEntity.userInformation.profileImage,
                creatorEntity.creatorProfile.mainSubject,
                userEntity.userInformation.email,
                creatorEntity.creatorProfile.introduction)
            .from(creatorEntity)
            .where(creatorEntity.id.eq(creatorId))
            .leftJoin(userEntity).on(userEntity.uid.eq(creatorEntity.userId))
            .fetchFirst();

        if (result == null) {
            throw new CustomException(Error.NOT_FOUND);

        }

        return CreatorInformation.builder()
            .creatorId(result.get(creatorEntity.id))
            .name(result.get(userEntity.userInformation.name))
            .profileImage(result.get(userEntity.userInformation.profileImage))
            .mainSubject(result.get(creatorEntity.creatorProfile.mainSubject))
            .email(result.get(userEntity.userInformation.email))
            .salesCount(salesCount)
            .rate(rate)
            .introduction(result.get(creatorEntity.creatorProfile.introduction))
            .build();
    }

    @Override
    public Creator save(Creator creator) {
        return creatorJpaRepository.save(CreatorEntity.from(creator)).toModel();
    }

    //TODO library 도메인 추가 후 로직 복구
    private Integer getCreatorSalesCount(Long creatorId) {
        return 1;
//        return jpaQueryFactory
//            .select(userQuestionLibraryEntity.id.countDistinct().intValue())
//            .from(questionEntity)
//            .leftJoin(userQuestionLibraryEntity).on(userQuestionLibraryEntity.questionId.eq(questionEntity.id))
//            .where(questionEntity.creatorId.eq(creatorId))
//            .fetchFirst();
    }

    //TODO library 도메인 추가 후 로직 복구
    private Double getCreatorRate(Long creatorId) {
        return 1.0;
//        return jpaQueryFactory.select(
//                MathExpressions.round(questionReviewEntity.rate.avg(), 1).coalesce(0.0))
//            .from(questionEntity)
//            .leftJoin(questionReviewEntity).on(questionReviewEntity.questionId.eq(questionEntity.id))
//            .where(questionEntity.creatorId.eq(creatorId))
//            .fetchFirst();
    }
}
