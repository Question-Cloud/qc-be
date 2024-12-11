package com.eager.questioncloud.application.api.feed.subscribe.controller;

import com.eager.questioncloud.application.api.common.PagingResponse;
import com.eager.questioncloud.application.api.feed.subscribe.service.FeedSubscribeService;
import com.eager.questioncloud.application.security.UserPrincipal;
import com.eager.questioncloud.core.common.PagingInformation;
import com.eager.questioncloud.core.domain.creator.dto.CreatorInformation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/feed/subscribe")
@RequiredArgsConstructor
public class FeedSubscribeController {
    private final FeedSubscribeService feedSubscribeService;

    @GetMapping("/my-subscribe")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청 성공")
    })
    @Operation(operationId = "나의 구독 목록 조회", summary = "나의 구독 목록 조회", tags = {"subscribe"}, description = "나의 구독 목록 조회")
    @Parameter(name = "size", description = "paging size", schema = @Schema(type = "integer"))
    @Parameter(name = "page", description = "paging page", schema = @Schema(type = "integer"))
    public PagingResponse<CreatorInformation> getMySubscribeList(
        @AuthenticationPrincipal UserPrincipal userPrincipal, PagingInformation pagingInformation) {
        int total = feedSubscribeService.countMySubscribe(userPrincipal.getUser().getUid());
        List<CreatorInformation> subscribeCreators = feedSubscribeService.getMySubscribeCreators(userPrincipal.getUser().getUid(), pagingInformation);
        return new PagingResponse<>(total, subscribeCreators);
    }
}