package com.eager.questioncloud.application.api.feed.subscribe.controller

import com.eager.questioncloud.application.api.common.PagingResponse
import com.eager.questioncloud.application.api.creator.dto.CreatorInformation
import com.eager.questioncloud.application.api.feed.subscribe.service.FeedSubscribeService
import com.eager.questioncloud.application.security.UserPrincipal
import com.eager.questioncloud.core.common.PagingInformation
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
    fun getMySubscribeList(
        @AuthenticationPrincipal userPrincipal: UserPrincipal, pagingInformation: PagingInformation
    ): PagingResponse<CreatorInformation> {
        val total = feedSubscribeService.countMySubscribe(userPrincipal.user.uid)
        val subscribeCreators = feedSubscribeService.getMySubscribes(
            userPrincipal.user.uid, pagingInformation
        )
        return PagingResponse(total, subscribeCreators)
    }
}
