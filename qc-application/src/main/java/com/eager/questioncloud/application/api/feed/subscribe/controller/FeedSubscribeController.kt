package com.eager.questioncloud.application.api.feed.subscribe.controller

import com.eager.questioncloud.application.api.common.PagingResponse
import com.eager.questioncloud.application.business.creator.dto.CreatorInformation
import com.eager.questioncloud.application.business.subscribe.service.FeedSubscribeService
import com.eager.questioncloud.application.security.UserPrincipal
import com.eager.questioncloud.core.common.PagingInformation
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/feed/subscribe")
class FeedSubscribeController(
    private val feedSubscribeService: FeedSubscribeService
) {
    @GetMapping("/my-subscribe")
    @ApiResponses(value = [ApiResponse(responseCode = "200", description = "요청 성공")])
    @Operation(operationId = "나의 구독 목록 조회", summary = "나의 구독 목록 조회", tags = ["subscribe"], description = "나의 구독 목록 조회")
    @Parameter(name = "size", description = "paging size", schema = Schema(type = "integer"))
    @Parameter(name = "page", description = "paging page", schema = Schema(type = "integer"))
    fun getMySubscribeList(
        @AuthenticationPrincipal userPrincipal: UserPrincipal, pagingInformation: PagingInformation
    ): PagingResponse<CreatorInformation> {
        val total = feedSubscribeService.countMySubscribe(userPrincipal.user.uid!!)
        val subscribeCreators = feedSubscribeService.getMySubscribes(
            userPrincipal.user.uid!!, pagingInformation
        )
        return PagingResponse(total, subscribeCreators)
    }
}
