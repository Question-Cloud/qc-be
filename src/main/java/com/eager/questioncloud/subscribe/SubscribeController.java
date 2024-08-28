package com.eager.questioncloud.subscribe;

import com.eager.questioncloud.common.DefaultResponse;
import com.eager.questioncloud.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/subscribe")
@RequiredArgsConstructor
public class SubscribeController {
    private final SubscribeService subscribeService;

    @PostMapping("/{creatorId}")
    public DefaultResponse subscribe(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Long creatorId) {
        subscribeService.subscribe(userPrincipal.getUser().getUid(), creatorId);
        return DefaultResponse.success();
    }
}
