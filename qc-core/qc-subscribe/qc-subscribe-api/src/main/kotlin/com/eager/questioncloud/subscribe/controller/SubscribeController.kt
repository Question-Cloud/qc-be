package com.eager.questioncloud.subscribe.controller

import com.eager.questioncloud.common.auth.UserPrincipal
import com.eager.questioncloud.common.dto.DefaultResponse
import com.eager.questioncloud.common.dto.PagingResponse
import com.eager.questioncloud.common.pagination.PagingInformation
import com.eager.questioncloud.subscribe.dto.CreatorSubscribeInformationResponse
import com.eager.questioncloud.subscribe.dto.UserSubscriptionDetail
import com.eager.questioncloud.subscribe.service.SubscribeService
import com.eager.questioncloud.subscribe.service.UserSubscriptionService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/subscribe")
class SubscribeController(
    private val subscribeService: SubscribeService,
    private val userSubscriptionService: UserSubscriptionService
) {
    @GetMapping("/status/{creatorId}")
    fun isSubscribed(
        userPrincipal: UserPrincipal,
        @PathVariable creatorId: Long
    ): CreatorSubscribeInformationResponse {
        val isSubscribed = userSubscriptionService.isSubscribed(userPrincipal.userId, creatorId)
        return CreatorSubscribeInformationResponse(isSubscribed)
    }
    
    @GetMapping("/my-subscribe")
    fun getMySubscribe(
        userPrincipal: UserPrincipal, pagingInformation: PagingInformation
    ): PagingResponse<UserSubscriptionDetail> {
        val total = userSubscriptionService.countMySubscribe(userPrincipal.userId)
        val subscribedCreatorInformation = userSubscriptionService.getMySubscribes(userPrincipal.userId, pagingInformation)
        return PagingResponse(total, subscribedCreatorInformation)
    }
    
    @PostMapping("/{creatorId}")
    fun subscribe(
        userPrincipal: UserPrincipal,
        @PathVariable creatorId: Long
    ): DefaultResponse {
        subscribeService.subscribe(userPrincipal.userId, creatorId)
        return DefaultResponse.success()
    }
    
    @DeleteMapping("/{creatorId}")
    fun unSubscribe(
        userPrincipal: UserPrincipal,
        @PathVariable creatorId: Long
    ): DefaultResponse {
        subscribeService.unSubscribe(userPrincipal.userId, creatorId)
        return DefaultResponse.success()
    }
}
