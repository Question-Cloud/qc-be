package com.eager.questioncloud.storage.coupon;

import static com.eager.questioncloud.storage.coupon.QCouponEntity.couponEntity;
import static com.eager.questioncloud.storage.coupon.QUserCouponEntity.userCouponEntity;

import com.eager.questioncloud.core.domain.coupon.dto.UserCouponDto.AvailableUserCouponItem;
import com.eager.questioncloud.core.domain.coupon.model.UserCoupon;
import com.eager.questioncloud.core.domain.coupon.repository.UserCouponRepository;
import com.eager.questioncloud.core.exception.CustomException;
import com.eager.questioncloud.core.exception.Error;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserCouponRepositoryImpl implements UserCouponRepository {
    private final JPAQueryFactory jpaQueryFactory;
    private final UserCouponJpaRepository userCouponJpaRepository;

    @Override
    public UserCoupon getUserCoupon(Long userCouponId, Long userId) {
        Tuple tuple = jpaQueryFactory.select(userCouponEntity, couponEntity)
            .from(userCouponEntity)
            .where(userCouponEntity.id.eq(userCouponId), userCouponEntity.userId.eq(userCouponId))
            .innerJoin(couponEntity).on(couponEntity.id.eq(userCouponEntity.couponId))
            .fetchFirst();

        if (tuple == null) {
            throw new CustomException(Error.WRONG_COUPON);
        }

        return tuple.get(userCouponEntity).toModel(tuple.get(couponEntity));
    }

    @Override
    public UserCoupon getUserCoupon(Long userCouponId) {
        Tuple tuple = jpaQueryFactory.select(userCouponEntity, couponEntity)
            .from(userCouponEntity)
            .where(userCouponEntity.id.eq(userCouponId))
            .innerJoin(couponEntity).on(couponEntity.id.eq(userCouponEntity.couponId))
            .fetchFirst();

        if (tuple == null) {
            throw new CustomException(Error.WRONG_COUPON);
        }

        return tuple.get(userCouponEntity).toModel(tuple.get(couponEntity));
    }

    @Override
    public Boolean isRegistered(Long userId, Long couponId) {
        return userCouponJpaRepository.existsByUserIdAndCouponId(userId, couponId);
    }

    @Override
    public UserCoupon save(UserCoupon userCoupon) {
        UserCouponEntity result = userCouponJpaRepository.save(UserCouponEntity.from(userCoupon));

        return UserCoupon.builder()
            .id(result.getId())
            .userId(result.getUserId())
            .coupon(userCoupon.getCoupon())
            .isUsed(result.getIsUsed())
            .createdAt(result.getCreatedAt())
            .build();
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
                    couponEntity.endAt))
            .from(userCouponEntity)
            .leftJoin(couponEntity).on(couponEntity.id.eq(userCouponEntity.couponId))
            .where(userCouponEntity.userId.eq(userId), userCouponEntity.isUsed.isFalse(), couponEntity.endAt.after(LocalDateTime.now()))
            .fetch();
    }
}
