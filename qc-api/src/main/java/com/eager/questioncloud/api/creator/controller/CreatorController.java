package com.eager.questioncloud.api.creator.controller;

import com.eager.questioncloud.api.creator.controller.Response.CreatorInformationResponse;
import com.eager.questioncloud.api.creator.controller.Response.CreatorProfileResponse;
import com.eager.questioncloud.api.creator.controller.Response.RegisterCreatorResponse;
import com.eager.questioncloud.common.DefaultResponse;
import com.eager.questioncloud.core.domain.creator.dto.CreatorDto.CreatorInformation;
import com.eager.questioncloud.core.domain.creator.model.Creator;
import com.eager.questioncloud.core.domain.creator.service.CreatorService;
import com.eager.questioncloud.core.domain.creator.vo.CreatorProfile;
import com.eager.questioncloud.core.domain.user.dto.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/creator")
@RequiredArgsConstructor
public class CreatorController {
    private final CreatorService creatorService;

    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('ROLE_CreatorUser')")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청 성공")
    })
    @Operation(operationId = "크리에이터 정보 조회 (나)", summary = "크리에이터 정보 조회 (나)", tags = {"creator"}, description = "크리에이터 정보 조회 (나)")
    public CreatorProfileResponse getMyCreatorInformation(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        CreatorProfile profile = userPrincipal.getCreator().getCreatorProfile();
        return new CreatorProfileResponse(profile);
    }

    @PatchMapping("/me")
    @PreAuthorize("hasAnyRole('ROLE_CreatorUser')")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청 성공")
    })
    @Operation(operationId = "크리에이터 정보 수정", summary = "크리에이터 정보 수정", tags = {"creator"}, description = "크리에이터 정보 수정")
    public DefaultResponse updateMyCreatorInformation(
        @AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody @Valid Request.UpdateCreatorProfileRequest request) {
        creatorService.updateCreatorProfile(userPrincipal.getCreator(), new CreatorProfile(request.getMainSubject(), request.getIntroduction()));
        return DefaultResponse.success();
    }

    @GetMapping("/{creatorId}")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청 성공")
    })
    @Operation(operationId = "크리에이터 정보 조회", summary = "크리에이터 정보 조회", tags = {"creator"}, description = "크리에이터 정보 조회")
    public CreatorInformationResponse getCreatorInformation(@PathVariable Long creatorId) {
        CreatorInformation creatorInformation = creatorService.getCreatorInformation(creatorId);
        return new CreatorInformationResponse(creatorInformation);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_NormalUser')")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청 성공")
    })
    @Operation(operationId = "크리에이터 등록 신청", summary = "크리에이터 등록 신청", tags = {"creator"}, description = "크리에이터 등록 신청")
    public RegisterCreatorResponse registerCreator(
        @AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody @Valid Request.RegisterCreatorRequest request) {
        Creator creator = creatorService.register(
            userPrincipal.getUser().getUid(),
            new CreatorProfile(request.getMainSubject(), request.getIntroduction()));
        return new RegisterCreatorResponse(creator.getId());
    }
}
