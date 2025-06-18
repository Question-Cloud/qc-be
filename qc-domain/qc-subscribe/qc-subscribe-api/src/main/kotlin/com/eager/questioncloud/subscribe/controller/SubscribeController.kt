package com.eager.questioncloud.subscribe.controller

import com.eager.questioncloud.common.auth.UserPrincipal
import com.eager.questioncloud.common.dto.DefaultResponse
import com.eager.questioncloud.common.dto.PagingResponse
import com.eager.questioncloud.common.pagination.PagingInformation
import com.eager.questioncloud.subscribe.dto.CreatorSubscribeInformationResponse
import com.eager.questioncloud.subscribe.dto.SubscribedCreatorInformation
import com.eager.questioncloud.subscribe.service.SubscribeService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/subscribe")
class SubscribeController(
    private val subscribeService: SubscribeService
) {
    @GetMapping("/status/{creatorId}")
    fun isSubscribed(
        userPrincipal: UserPrincipal,
        @PathVariable creatorId: Long
    ): CreatorSubscribeInformationResponse {
        val isSubscribed = subscribeService.isSubscribed(userPrincipal.userId, creatorId)
        val countSubscriber = subscribeService.countCreatorSubscriber(creatorId)
        return CreatorSubscribeInformationResponse(isSubscribed, countSubscriber)
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

    @GetMapping("/my-subscribe")
    fun getMySubscribe(
        userPrincipal: UserPrincipal, pagingInformation: PagingInformation
    ): PagingResponse<SubscribedCreatorInformation> {
        val total = subscribeService.countMySubscribe(userPrincipal.userId)
        val subscribedCreatorInformation = subscribeService.getMySubscribes(userPrincipal.userId, pagingInformation)
        return PagingResponse(total, subscribedCreatorInformation)
    }
}
