package com.eager.questioncloud.application.api.subscribe.controller

import com.eager.questioncloud.application.api.common.DefaultResponse
import com.eager.questioncloud.application.api.common.DefaultResponse.Companion.success
import com.eager.questioncloud.application.api.subscribe.dto.CreatorSubscribeInformationResponse
import com.eager.questioncloud.application.api.subscribe.service.SubscribeService
import com.eager.questioncloud.application.security.UserPrincipal
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/subscribe")
class SubscribeController(
    private val subscribeService: SubscribeService
) {
    @GetMapping("/status/{creatorId}")
    fun isSubscribed(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        @PathVariable creatorId: Long
    ): CreatorSubscribeInformationResponse {
        val isSubscribed = subscribeService.isSubscribed(userPrincipal.user.uid, creatorId)
        val countSubscriber = subscribeService.countSubscriber(creatorId)
        return CreatorSubscribeInformationResponse(isSubscribed, countSubscriber)
    }

    @PostMapping("/{creatorId}")
    fun subscribe(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        @PathVariable creatorId: Long
    ): DefaultResponse {
        subscribeService.subscribe(userPrincipal.user.uid, creatorId)
        return success()
    }

    @DeleteMapping("/{creatorId}")
    fun unSubscribe(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        @PathVariable creatorId: Long
    ): DefaultResponse {
        subscribeService.unSubscribe(userPrincipal.user.uid, creatorId)
        return success()
    }
}
