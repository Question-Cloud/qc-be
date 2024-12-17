package com.eager.questioncloud.core.domain.coupon.infrastructure;

import static com.eager.questioncloud.core.domain.coupon.infrastructure.QCouponEntity.couponEntity;

import com.eager.questioncloud.core.domain.coupon.model.Coupon;
import com.eager.questioncloud.core.exception.CoreException;
import com.eager.questioncloud.core.exception.Error;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CouponRepositoryImpl implements CouponRepository {
    private final CouponJpaRepository couponJpaRepository;
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Coupon findById(Long id) {
        return couponJpaRepository.findById(id)
            .orElseThrow(() -> new CoreException(Error.WRONG_COUPON))
            .toDomain();
    }

    @Override
    public Coupon findByCode(String code) {
        return couponJpaRepository.findByCode(code)
            .orElseThrow(() -> new CoreException(Error.NOT_FOUND))
            .toDomain();
    }

    @Override
    public Coupon save(Coupon coupon) {
        return couponJpaRepository.save(CouponEntity.from(coupon)).toDomain();
    }

    @Override
    public Boolean decreaseCount(Long couponId) {
        long result = jpaQueryFactory.update(couponEntity)
            .set(couponEntity.remainingCount, couponEntity.remainingCount.subtract(1))
            .where(couponEntity.id.eq(couponId), couponEntity.remainingCount.goe(1))
            .execute();
        return result == 1;
    }
}
