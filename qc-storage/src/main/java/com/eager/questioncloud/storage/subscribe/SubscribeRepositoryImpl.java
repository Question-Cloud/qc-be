package com.eager.questioncloud.storage.subscribe;

import static com.eager.questioncloud.storage.creator.QCreatorEntity.creatorEntity;
import static com.eager.questioncloud.storage.creator.QCreatorStatisticsEntity.creatorStatisticsEntity;
import static com.eager.questioncloud.storage.subscribe.QSubscribeEntity.subscribeEntity;
import static com.eager.questioncloud.storage.user.QUserEntity.userEntity;

import com.eager.questioncloud.core.common.PagingInformation;
import com.eager.questioncloud.core.domain.creator.dto.CreatorDto.CreatorInformation;
import com.eager.questioncloud.core.domain.subscribe.model.Subscribe;
import com.eager.questioncloud.core.domain.subscribe.repository.SubscribeRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SubscribeRepositoryImpl implements SubscribeRepository {
    private final SubscribeJpaRepository subscribeJpaRepository;
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Subscribe save(Subscribe subscribe) {
        return subscribeJpaRepository.save(SubscribeEntity.from(subscribe)).toModel();
    }

    @Override
    public Boolean isSubscribed(Long subscriberId, Long creatorId) {
        return subscribeJpaRepository.existsBySubscriberIdAndCreatorId(subscriberId, creatorId);
    }

    @Override
    public void unSubscribe(Long subscriberId, Long creatorId) {
        subscribeJpaRepository.deleteBySubscriberIdAndCreatorId(subscriberId, creatorId);
    }

    @Override
    public int countSubscriber(Long creatorId) {
        return subscribeJpaRepository.countByCreatorId(creatorId);
    }

    @Override
    public List<CreatorInformation> getSubscribeCreators(Long userId, PagingInformation pagingInformation) {
        return jpaQueryFactory.select(
                Projections.constructor(
                    CreatorInformation.class,
                    creatorEntity.id,
                    userEntity.userInformationEntity.name,
                    userEntity.userInformationEntity.profileImage,
                    creatorEntity.creatorProfileEntity.mainSubject,
                    userEntity.userInformationEntity.email,
                    creatorStatisticsEntity.salesCount,
                    creatorStatisticsEntity.averageRateOfReview,
                    creatorEntity.creatorProfileEntity.introduction))
            .from(subscribeEntity)
            .where(subscribeEntity.subscriberId.eq(userId))
            .leftJoin(creatorEntity).on(creatorEntity.id.eq(subscribeEntity.creatorId))
            .leftJoin(userEntity).on(userEntity.uid.eq(creatorEntity.userId))
            .leftJoin(creatorStatisticsEntity).on(creatorStatisticsEntity.creatorId.eq(creatorEntity.id))
            .offset(pagingInformation.getOffset())
            .limit(pagingInformation.getSize())
            .fetch();
    }

    @Override
    public int countMySubscribe(Long userId) {
        Integer count = jpaQueryFactory.select(subscribeEntity.id.count().intValue())
            .from(subscribeEntity)
            .where(subscribeEntity.subscriberId.eq(userId))
            .fetchFirst();

        if (count == null) {
            return 0;
        }

        return count;
    }
}
