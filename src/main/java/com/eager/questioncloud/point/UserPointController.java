package com.eager.questioncloud.point;

import com.eager.questioncloud.point.Response.GetUserPointResponse;
import com.eager.questioncloud.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/point")
@RequiredArgsConstructor
public class UserPointController {
    private final UserPointService userPointService;

    @GetMapping
    public GetUserPointResponse getUserPoint(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        int userPoint = userPointService.getUserPoint(userPrincipal.getUser().getUid());
        return new GetUserPointResponse(userPoint);
    }
}
