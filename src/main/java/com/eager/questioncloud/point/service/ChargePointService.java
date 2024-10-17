package com.eager.questioncloud.point.service;

import com.eager.questioncloud.point.implement.ChargePointOrderAppender;
import com.eager.questioncloud.point.model.ChargePointOrder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChargePointService {
    private final ChargePointOrderAppender chargePointOrderAppender;

    public ChargePointOrder createOrder(ChargePointOrder chargePointOrder) {
        return chargePointOrderAppender.append(chargePointOrder);
    }
}
