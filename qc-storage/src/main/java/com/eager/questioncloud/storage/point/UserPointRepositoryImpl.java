package com.eager.questioncloud.storage.point;

import static com.eager.questioncloud.storage.point.QUserPointEntity.userPointEntity;

import com.eager.questioncloud.core.domain.user.repository.UserPointRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserPointRepositoryImpl implements UserPointRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public int getPoint(Long userId) {
        Integer point = jpaQueryFactory.select(userPointEntity.point.intValue())
            .from(userPointEntity)
            .where(userPointEntity.userId.eq(userId))
            .fetchFirst();

        if (point == null) {
            return 0;
        }

        return point;
    }

    @Override
    public void updatePoint(Long userId, int point) {
        jpaQueryFactory.update(userPointEntity)
            .set(userPointEntity.point, point)
            .where(userPointEntity.userId.eq(userId))
            .execute();
    }
}