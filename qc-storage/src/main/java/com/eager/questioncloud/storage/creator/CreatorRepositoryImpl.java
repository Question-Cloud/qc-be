package com.eager.questioncloud.storage.creator;

import static com.eager.questioncloud.storage.creator.QCreatorEntity.creatorEntity;
import static com.eager.questioncloud.storage.creator.QCreatorStatisticsEntity.creatorStatisticsEntity;
import static com.eager.questioncloud.storage.user.QUserEntity.userEntity;

import com.eager.questioncloud.core.domain.creator.dto.CreatorInformation;
import com.eager.questioncloud.core.domain.creator.model.Creator;
import com.eager.questioncloud.core.domain.creator.repository.CreatorRepository;
import com.eager.questioncloud.core.exception.CustomException;
import com.eager.questioncloud.core.exception.Error;
import com.querydsl.core.types.Projections;
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
        CreatorInformation creatorInformation = jpaQueryFactory.select(Projections.constructor(
                CreatorInformation.class,
                creatorEntity.id,
                userEntity.userInformationEntity.name,
                userEntity.userInformationEntity.profileImage,
                creatorEntity.creatorProfileEntity.mainSubject,
                userEntity.userInformationEntity.email,
                creatorStatisticsEntity.salesCount,
                creatorStatisticsEntity.averageRateOfReview,
                creatorEntity.creatorProfileEntity.introduction))
            .from(creatorEntity)
            .where(creatorEntity.id.eq(creatorId))
            .leftJoin(userEntity).on(userEntity.uid.eq(creatorEntity.userId))
            .leftJoin(creatorStatisticsEntity).on(creatorStatisticsEntity.creatorId.eq(creatorEntity.id))
            .fetchFirst();

        if (creatorInformation == null) {
            throw new CustomException(Error.NOT_FOUND);
        }

        return creatorInformation;
    }

    @Override
    public Creator save(Creator creator) {
        return creatorJpaRepository.save(CreatorEntity.from(creator)).toModel();
    }
}
