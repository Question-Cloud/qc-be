package com.eager.questioncloud.application.api.payment.question.controller;

import com.eager.questioncloud.application.api.common.DefaultResponse;
import com.eager.questioncloud.application.api.common.PagingResponse;
import com.eager.questioncloud.application.api.payment.question.dto.QuestionPaymentControllerRequest.QuestionPaymentRequest;
import com.eager.questioncloud.application.business.payment.question.service.QuestionOrderService;
import com.eager.questioncloud.application.business.payment.question.service.QuestionPaymentCouponService;
import com.eager.questioncloud.application.business.payment.question.service.QuestionPaymentHistoryService;
import com.eager.questioncloud.application.business.payment.question.service.QuestionPaymentService;
import com.eager.questioncloud.application.security.UserPrincipal;
import com.eager.questioncloud.core.common.PagingInformation;
import com.eager.questioncloud.core.domain.payment.model.QuestionOrder;
import com.eager.questioncloud.core.domain.payment.model.QuestionPaymentCoupon;
import com.eager.questioncloud.core.domain.payment.model.QuestionPaymentHistory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payment/question")
@RequiredArgsConstructor
public class QuestionPaymentController {
    private final QuestionPaymentService questionPaymentService;
    private final QuestionOrderService questionOrderService;
    private final QuestionPaymentCouponService questionPaymentCouponService;
    private final QuestionPaymentHistoryService questionPaymentHistoryService;

    @PostMapping
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청 성공")
    })
    @Operation(operationId = "문제 구매", summary = "문제 구매", tags = {"question-payment"}, description = "문제 구매")
    public DefaultResponse questionPayment(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody QuestionPaymentRequest request) {
        QuestionOrder questionOrder = questionOrderService.generateQuestionOrder(userPrincipal.getUser().getUid(), request.getQuestionIds());
        QuestionPaymentCoupon questionPaymentCoupon = questionPaymentCouponService.getQuestionPaymentCoupon(
            request.getUserCouponId(),
            userPrincipal.getUser().getUid()
        );
        questionPaymentService.payment(userPrincipal.getUser().getUid(), questionOrder, questionPaymentCoupon);
        return DefaultResponse.success();
    }

    @GetMapping("/history")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청 성공")
    })
    @Operation(operationId = "문제 구매 내역 조회", summary = "문제 구매 내역 조회", tags = {"question-payment"}, description = "문제 구매 내역 조회")
    public PagingResponse<QuestionPaymentHistory> getQuestionPaymentHistory(
        @AuthenticationPrincipal UserPrincipal userPrincipal, PagingInformation pagingInformation) {
        return new PagingResponse<>(
            questionPaymentHistoryService.countQuestionPaymentHistory(userPrincipal.getUser().getUid()),
            questionPaymentHistoryService.getQuestionPaymentHistory(userPrincipal.getUser().getUid(), pagingInformation));
    }
}
