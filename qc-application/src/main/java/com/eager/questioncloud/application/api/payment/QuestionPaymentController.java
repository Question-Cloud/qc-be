package com.eager.questioncloud.application.api.payment;

import com.eager.questioncloud.application.api.common.DefaultResponse;
import com.eager.questioncloud.application.api.common.PagingResponse;
import com.eager.questioncloud.application.api.payment.QuestionPaymentControllerRequest.QuestionPaymentRequest;
import com.eager.questioncloud.application.security.UserPrincipal;
import com.eager.questioncloud.core.common.PagingInformation;
import com.eager.questioncloud.core.domain.payment.dto.QuestionPaymentHistory;
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
    private final QuestionPaymentHistoryService questionPaymentHistoryService;

    @PostMapping
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청 성공")
    })
    @Operation(operationId = "문제 구매", summary = "문제 구매", tags = {"question-payment"}, description = "문제 구매")
    public DefaultResponse questionPayment(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody QuestionPaymentRequest request) {
        questionPaymentService.payment(userPrincipal.getUser().getUid(), request.getQuestionIds(), request.getUserCouponId());
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
