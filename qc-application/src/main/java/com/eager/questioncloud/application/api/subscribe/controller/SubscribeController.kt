package com.eager.questioncloud.application.api.subscribe.controller

import com.eager.questioncloud.application.api.common.DefaultResponse
import com.eager.questioncloud.application.api.common.DefaultResponse.Companion.success
import com.eager.questioncloud.application.api.subscribe.dto.CreatorSubscribeInformationResponse
import com.eager.questioncloud.application.api.subscribe.service.SubscribeService
import com.eager.questioncloud.application.security.UserPrincipal
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/subscribe")
class SubscribeController(
    private val subscribeService: SubscribeService
) {
    @GetMapping("/status/{creatorId}")
    @ApiResponses(value = [ApiResponse(responseCode = "200", description = "요청 성공")])
    @Operation(
        operationId = "특정 크리에이터 구독 여부 확인", summary = "특정 크리에이터 구독 여부 확인", tags = ["subscribe"], description = """
                특정 크리에이터를 구독했는지 여부를 반환합니다.            
            """
    )
    fun isSubscribed(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        @PathVariable creatorId: Long
    ): CreatorSubscribeInformationResponse {
        val isSubscribed = subscribeService.isSubscribed(userPrincipal.user.uid!!, creatorId)
        val countSubscriber = subscribeService.countSubscriber(creatorId)
        return CreatorSubscribeInformationResponse(isSubscribed, countSubscriber)
    }

    @PostMapping("/{creatorId}")
    @ApiResponses(value = [ApiResponse(responseCode = "200", description = "요청 성공")])
    @Operation(operationId = "크리에이터 구독", summary = "크리에이터 구독", tags = ["subscribe"], description = "크리에이터 구독")
    fun subscribe(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        @PathVariable creatorId: Long
    ): DefaultResponse {
        subscribeService.subscribe(userPrincipal.user.uid!!, creatorId)
        return success()
    }

    @DeleteMapping("/{creatorId}")
    @ApiResponses(value = [ApiResponse(responseCode = "200", description = "요청 성공")])
    @Operation(operationId = "크리에이터 구독 취소", summary = "크리에이터 구독 취소", tags = ["subscribe"], description = "크리에이터 구독 취소")
    fun unSubscribe(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        @PathVariable creatorId: Long
    ): DefaultResponse {
        subscribeService.unSubscribe(userPrincipal.user.uid!!, creatorId)
        return success()
    }
}
