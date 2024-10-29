package com.eager.questioncloud.core.domain.user.point.implement;

import com.eager.questioncloud.core.domain.user.point.repository.ChargePointOrderRepository;
import com.eager.questioncloud.core.domain.user.point.model.ChargePointOrder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChargePointOrderAppender {
    private final ChargePointOrderRepository chargePointOrderRepository;

    public ChargePointOrder append(ChargePointOrder chargePointOrder) {
        return chargePointOrderRepository.append(chargePointOrder);
    }
}
