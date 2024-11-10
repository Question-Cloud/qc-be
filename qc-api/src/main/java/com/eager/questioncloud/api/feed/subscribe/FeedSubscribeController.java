package com.eager.questioncloud.api.feed.subscribe;

import com.eager.questioncloud.api.feed.subscribe.Response.CreatorSubscribeInformationResponse;
import com.eager.questioncloud.common.DefaultResponse;
import com.eager.questioncloud.common.PagingResponse;
import com.eager.questioncloud.core.common.PagingInformation;
import com.eager.questioncloud.core.domain.feed.subscribe.dto.SubscribeDto.SubscribeListItem;
import com.eager.questioncloud.core.domain.feed.subscribe.service.SubscribeService;
import com.eager.questioncloud.security.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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

    @GetMapping("/{creatorId}")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청 성공")
    })
    @Operation(operationId = "특정 크리에이터 구독 여부 확인", summary = "특정 크리에이터 구독 여부 확인", tags = {"subscribe"},
        description = """
                특정 크리에이터를 구독했는지 여부를 반환합니다.
            """)
    public CreatorSubscribeInformationResponse isSubscribed(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Long creatorId) {
        Boolean isSubscribed = subscribeService.isSubscribed(userPrincipal.getUser().getUid(), creatorId);
        int countSubscriber = subscribeService.countSubscriber(creatorId);
        return new CreatorSubscribeInformationResponse(isSubscribed, countSubscriber);
    }

    @PostMapping("/{creatorId}")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청 성공")
    })
    @Operation(operationId = "크리에이터 구독", summary = "크리에이터 구독", tags = {"subscribe"}, description = "크리에이터 구독")
    public DefaultResponse subscribe(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Long creatorId) {
        subscribeService.subscribe(userPrincipal.getUser().getUid(), creatorId);
        return DefaultResponse.success();
    }

    @DeleteMapping("/{creatorId}")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청 성공")
    })
    @Operation(operationId = "크리에이터 구독 취소", summary = "크리에이터 구독 취소", tags = {"subscribe"}, description = "크리에이터 구독 취소")
    public DefaultResponse unSubscribe(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Long creatorId) {
        subscribeService.unSubscribe(userPrincipal.getUser().getUid(), creatorId);
        return DefaultResponse.success();
    }
}
