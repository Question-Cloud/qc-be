package com.eager.questioncloud.payment;

import com.eager.questioncloud.common.DefaultResponse;
import com.eager.questioncloud.payment.Request.QuestionPaymentRequest;
import com.eager.questioncloud.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payment/question")
@RequiredArgsConstructor
public class QuestionPaymentController {
    private final QuestionPaymentService questionPaymentService;

    @PostMapping
    public DefaultResponse questionPayment(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody QuestionPaymentRequest request) {
        questionPaymentService.payment(userPrincipal.getUser().getUid(), request.getQuestionIds(), request.getCouponId());
        return DefaultResponse.success();
    }
}
