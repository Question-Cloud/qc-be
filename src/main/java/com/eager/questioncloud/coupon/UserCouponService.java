package com.eager.questioncloud.coupon;

import com.eager.questioncloud.coupon.UserCouponDto.AvailableUserCouponItem;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserCouponService {
    private final UserCouponCreator userCouponCreator;
    private final UserCouponReader userCouponReader;

    public UserCoupon registerCoupon(Long userId, String couponCode) {
        return userCouponCreator.registerCoupon(userId, couponCode);
    }

    public List<AvailableUserCouponItem> getAvailableUserCoupons(Long userId) {
        return userCouponReader.getAvailableUserCoupons(userId);
    }
}
