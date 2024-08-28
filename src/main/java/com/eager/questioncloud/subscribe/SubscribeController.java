package com.eager.questioncloud.subscribe;

import com.eager.questioncloud.common.DefaultResponse;
import com.eager.questioncloud.security.UserPrincipal;
import com.eager.questioncloud.subscribe.Response.CreatorSubscribeInformationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/subscribe")
@RequiredArgsConstructor
public class SubscribeController {
    private final SubscribeService subscribeService;

    @GetMapping("/{creatorId}")
    public CreatorSubscribeInformationResponse isSubscribed(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Long creatorId) {
        Boolean isSubscribed = subscribeService.isSubscribed(userPrincipal.getUser().getUid(), creatorId);
        int countSubscriber = subscribeService.countSubscriber(creatorId);
        return new CreatorSubscribeInformationResponse(isSubscribed, countSubscriber);
    }

    @PostMapping("/{creatorId}")
    public DefaultResponse subscribe(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Long creatorId) {
        subscribeService.subscribe(userPrincipal.getUser().getUid(), creatorId);
        return DefaultResponse.success();
    }

    @DeleteMapping("/{creatorId}")
    public DefaultResponse unSubscribe(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Long creatorId) {
        subscribeService.unSubscribe(userPrincipal.getUser().getUid(), creatorId);
        return DefaultResponse.success();
    }
}
