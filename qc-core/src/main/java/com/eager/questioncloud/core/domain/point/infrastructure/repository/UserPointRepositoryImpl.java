package com.eager.questioncloud.core.domain.point.infrastructure.repository;

import static com.eager.questioncloud.core.domain.point.infrastructure.entity.QUserPointEntity.userPointEntity;

import com.eager.questioncloud.core.domain.point.infrastructure.entity.UserPointEntity;
import com.eager.questioncloud.core.domain.point.model.UserPoint;
import com.eager.questioncloud.core.exception.CoreException;
import com.eager.questioncloud.core.exception.Error;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.transaction.Transactional;
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
            .orElseThrow(() -> new CoreException(Error.NOT_FOUND))
            .toModel();
    }

    @Override
    public Boolean usePoint(Long userId, int amount) {
        return jpaQueryFactory.update(userPointEntity)
            .set(userPointEntity.point, userPointEntity.point.subtract(amount))
            .where(userPointEntity.userId.eq(userId), userPointEntity.point.goe(amount))
            .execute() == 1;
    }

    @Override
    @Transactional
    public void chargePoint(Long userId, int amount) {
        jpaQueryFactory.update(userPointEntity)
            .set(userPointEntity.point, userPointEntity.point.add(amount))
            .where(userPointEntity.userId.eq(userId))
            .execute();
    }

    @Override
    public UserPoint save(UserPoint userPoint) {
        return userPointJpaRepository.save(UserPointEntity.from(userPoint)).toModel();
    }
}
