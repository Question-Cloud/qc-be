package com.eager.questioncloud.application.api.creator;

import com.eager.questioncloud.application.api.creator.RegisterCreatorControllerRequest.RegisterCreatorRequest;
import com.eager.questioncloud.application.api.creator.RegisterCreatorControllerResponse.RegisterCreatorResponse;
import com.eager.questioncloud.application.security.UserPrincipal;
import com.eager.questioncloud.domain.creator.Creator;
import com.eager.questioncloud.domain.creator.CreatorProfile;
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
    private final CreatorRegisterService creatorRegisterService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_NormalUser')")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청 성공")
    })
    @Operation(operationId = "크리에이터 등록 신청", summary = "크리에이터 등록 신청", tags = {"creator"}, description = "크리에이터 등록 신청")
    public RegisterCreatorResponse registerCreator(
        @AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody @Valid RegisterCreatorRequest request) {
        Creator creator = creatorRegisterService.register(
            userPrincipal.getUser(), new CreatorProfile(request.getMainSubject(), request.getIntroduction()));
        return new RegisterCreatorResponse(creator.getId());
    }
}
