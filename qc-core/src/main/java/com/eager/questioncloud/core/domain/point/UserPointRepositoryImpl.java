package com.eager.questioncloud.core.domain.point;

import static com.eager.questioncloud.core.domain.point.QUserPointEntity.userPointEntity;

import com.eager.questioncloud.exception.CustomException;
import com.eager.questioncloud.exception.Error;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserPointRepositoryImpl implements UserPointRepository {
    private final UserPointJpaRepository userPointJpaRepository;
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public UserPoint getUserPoint(Long userId) {
        return userPointJpaRepository.findById(userId)
            .orElseThrow(() -> new CustomException(Error.NOT_FOUND))
            .toModel();
    }

    @Override
    public void updatePoint(Long userId, int point) {
        jpaQueryFactory.update(userPointEntity)
            .set(userPointEntity.point, point)
            .where(userPointEntity.userId.eq(userId))
            .execute();
    }

    @Override
    public UserPoint save(UserPoint userPoint) {
        return userPointJpaRepository.save(UserPointEntity.from(userPoint)).toModel();
    }
}
