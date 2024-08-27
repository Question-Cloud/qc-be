package com.eager.questioncloud.point;

import static com.eager.questioncloud.user.QUserEntity.userEntity;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserPointRepositoryImpl implements UserPointRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public int getPoint(Long userId) {
        Integer point = jpaQueryFactory.select(userEntity.point.intValue())
            .from(userEntity)
            .where(userEntity.uid.eq(userId))
            .fetchFirst();

        if (point == null) {
            return 0;
        }

        return point;
    }

    @Override
    public void updatePoint(Long userId, int point) {
        jpaQueryFactory.update(userEntity)
            .set(userEntity.point, point)
            .where(userEntity.uid.eq(userId))
            .execute();
    }
}
