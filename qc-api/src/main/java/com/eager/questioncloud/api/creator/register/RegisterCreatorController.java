package com.eager.questioncloud.api.creator.register;

import com.eager.questioncloud.api.creator.register.Response.RegisterCreatorResponse;
import com.eager.questioncloud.core.domain.creator.model.Creator;
import com.eager.questioncloud.core.domain.creator.service.CreatorService;
import com.eager.questioncloud.core.domain.creator.vo.CreatorProfile;
import com.eager.questioncloud.security.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/creator/register")
@RequiredArgsConstructor
public class RegisterCreatorController {
    private final CreatorService creatorService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_NormalUser')")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청 성공")
    })
    @Operation(operationId = "크리에이터 등록 신청", summary = "크리에이터 등록 신청", tags = {"creator"}, description = "크리에이터 등록 신청")
    public RegisterCreatorResponse registerCreator(
        @AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody @Valid Request.RegisterCreatorRequest request) {
        Creator creator = creatorService.register(
            userPrincipal.getUser(), new CreatorProfile(request.getMainSubject(), request.getIntroduction()));
        return new RegisterCreatorResponse(creator.getId());
    }
}
