package com.eager.questioncloud.subscribe.repository;

import static com.eager.questioncloud.creator.QCreatorEntity.creatorEntity;
import static com.eager.questioncloud.subscribe.QSubscribeEntity.subscribeEntity;
import static com.eager.questioncloud.user.QUserEntity.userEntity;

import com.eager.questioncloud.creator.dto.CreatorDto.CreatorSimpleInformation;
import com.eager.questioncloud.subscribe.QSubscribeEntity;
import com.eager.questioncloud.subscribe.domain.Subscribe;
import com.eager.questioncloud.subscribe.dto.SubscribeDto.SubscribeListItem;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SubscribeRepositoryImpl implements SubscribeRepository {
    private final SubscribeJpaRepository subscribeJpaRepository;
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Subscribe save(Subscribe subscribe) {
        return subscribeJpaRepository.save(subscribe.toEntity()).toModel();
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
    public List<SubscribeListItem> getMySubscribeList(Long userId, Pageable pageable) {
        QSubscribeEntity countCheck = new QSubscribeEntity("count");
        return jpaQueryFactory.select(
                Projections.constructor(
                    SubscribeListItem.class,
                    subscribeEntity.id,
                    Projections.constructor(CreatorSimpleInformation.class,
                        creatorEntity.id,
                        userEntity.name,
                        userEntity.profileImage,
                        creatorEntity.mainSubject,
                        creatorEntity.introduction,
                        countCheck.id.count().intValue())))
            .from(subscribeEntity)
            .where(subscribeEntity.subscriberId.eq(userId))
            .leftJoin(creatorEntity).on(creatorEntity.id.eq(subscribeEntity.creatorId))
            .leftJoin(userEntity).on(userEntity.uid.eq(creatorEntity.userId))
            .leftJoin(countCheck).on(countCheck.creatorId.eq(creatorEntity.id))
            .groupBy(subscribeEntity.id)
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
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
