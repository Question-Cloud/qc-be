package com.eager.questioncloud.creator;

import com.eager.questioncloud.common.DefaultResponse;
import com.eager.questioncloud.creator.CreatorDto.CreatorInformation;
import com.eager.questioncloud.creator.CreatorDto.MyCreatorInformation;
import com.eager.questioncloud.creator.Request.RegisterCreatorRequest;
import com.eager.questioncloud.creator.Request.UpdateMyCreatorInformationRequest;
import com.eager.questioncloud.creator.Response.CreatorInformationResponse;
import com.eager.questioncloud.creator.Response.MyCreatorInformationResponse;
import com.eager.questioncloud.creator.Response.RegisterCreatorResponse;
import com.eager.questioncloud.security.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    public MyCreatorInformationResponse getMyCreatorInformation(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        MyCreatorInformation information = creatorService.getMyCreatorInformation(userPrincipal.getUser().getUid());
        return new MyCreatorInformationResponse(information);
    }

    @PatchMapping("/me")
    public DefaultResponse updateMyCreatorInformation(
        @AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody @Valid UpdateMyCreatorInformationRequest request) {
        creatorService.updateMyCreatorInformation(userPrincipal.getUser().getUid(), request.getMainSubject(), request.getIntroduction());
        return DefaultResponse.success();
    }

    @GetMapping("/{creatorId}")
    public CreatorInformationResponse getCreatorInformation(@PathVariable Long creatorId) {
        CreatorInformation creatorInformation = creatorService.getCreatorInformation(creatorId);
        return new CreatorInformationResponse(creatorInformation);
    }

    @PostMapping
    public RegisterCreatorResponse registerCreator(
        @AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody @Valid RegisterCreatorRequest request) {
        Creator creator = creatorService.register(userPrincipal.getUser().getUid(), request.getMainSubject(), request.getIntroduction());
        return new RegisterCreatorResponse(creator.getId());
    }
}
