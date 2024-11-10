package com.eager.questioncloud.api.feed.subscribe;

import com.eager.questioncloud.common.PagingResponse;
import com.eager.questioncloud.core.common.PagingInformation;
import com.eager.questioncloud.core.domain.subscribe.dto.SubscribeDto.SubscribeListItem;
import com.eager.questioncloud.core.domain.subscribe.service.SubscribeService;
import com.eager.questioncloud.security.UserPrincipal;
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
    private final SubscribeService subscribeService;

    @GetMapping("/my-subscribe")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청 성공")
    })
    @Operation(operationId = "나의 구독 목록 조회", summary = "나의 구독 목록 조회", tags = {"subscribe"}, description = "나의 구독 목록 조회")
    @Parameter(name = "size", description = "paging size", schema = @Schema(type = "integer"))
    @Parameter(name = "page", description = "paging page", schema = @Schema(type = "integer"))
    public PagingResponse<SubscribeListItem> getMySubscribeList(
        @AuthenticationPrincipal UserPrincipal userPrincipal, PagingInformation pagingInformation) {
        int total = subscribeService.countMySubscribe(userPrincipal.getUser().getUid());
        List<SubscribeListItem> mySubscribeList = subscribeService.getMySubscribeList(userPrincipal.getUser().getUid(), pagingInformation);
        return new PagingResponse<>(total, mySubscribeList);
    }
}
