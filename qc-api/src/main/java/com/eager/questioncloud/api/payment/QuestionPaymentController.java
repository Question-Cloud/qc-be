package com.eager.questioncloud.api.payment;

import com.eager.questioncloud.api.payment.Request.QuestionPaymentRequest;
import com.eager.questioncloud.common.DefaultResponse;
import com.eager.questioncloud.core.domain.hub.payment.service.QuestionPaymentService;
import com.eager.questioncloud.core.domain.user.dto.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청 성공")
    })
    @Operation(operationId = "문제 구매", summary = "문제 구매", tags = {"question-payment"}, description = "문제 구매")
    public DefaultResponse questionPayment(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody QuestionPaymentRequest request) {
        questionPaymentService.payment(userPrincipal.getUser().getUid(), request.getQuestionIds(), request.getUserCouponId());
        return DefaultResponse.success();
    }
}
