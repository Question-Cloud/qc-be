package com.eager.questioncloud.storage.coupon;

import static com.eager.questioncloud.storage.coupon.QCouponEntity.couponEntity;
import static com.eager.questioncloud.storage.coupon.QUserCouponEntity.userCouponEntity;

import com.eager.questioncloud.core.domain.user.coupon.dto.UserCouponDto.AvailableUserCouponItem;
import com.eager.questioncloud.core.domain.user.coupon.model.UserCoupon;
import com.eager.questioncloud.core.domain.user.coupon.repository.UserCouponRepository;
import com.eager.questioncloud.core.exception.CustomException;
import com.eager.questioncloud.core.exception.Error;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserCouponRepositoryImpl implements UserCouponRepository {
    private final UserCouponJpaRepository userCouponJpaRepository;
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public UserCoupon getUserCoupon(Long userCouponId, Long userId) {
        return userCouponJpaRepository.findByIdAndUserIdAndIsUsedFalse(userCouponId, userId)
            .orElseThrow(() -> new CustomException(Error.WRONG_COUPON))
            .toModel();
    }

    @Override
    public Boolean isRegistered(Long userId, Long couponId) {
        return userCouponJpaRepository.existsByUserIdAndCouponId(userId, couponId);
    }

    @Override
    public UserCoupon save(UserCoupon userCoupon) {
        return userCouponJpaRepository.save(UserCouponEntity.from(userCoupon)).toModel();
    }

    @Override
    public List<AvailableUserCouponItem> getAvailableUserCoupons(Long userId) {
        return jpaQueryFactory.select(
                Projections.constructor(
                    AvailableUserCouponItem.class,
                    userCouponEntity.id,
                    couponEntity.title,
                    couponEntity.couponType,
                    couponEntity.value,
                    userCouponEntity.endAt))
            .from(userCouponEntity)
            .leftJoin(couponEntity).on(couponEntity.id.eq(userCouponEntity.couponId))
            .where(userCouponEntity.userId.eq(userId), userCouponEntity.isUsed.isFalse(), userCouponEntity.endAt.after(LocalDateTime.now()))
            .fetch();
    }
}
