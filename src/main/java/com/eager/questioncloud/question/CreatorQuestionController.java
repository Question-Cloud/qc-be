package com.eager.questioncloud.question;

import com.eager.questioncloud.common.DefaultResponse;
import com.eager.questioncloud.question.Request.ModifySelfMadeQuestionRequest;
import com.eager.questioncloud.question.Request.RegisterSelfMadeQuestionRequest;
import com.eager.questioncloud.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/question/self-made")
@RequiredArgsConstructor
public class CreatorQuestionController {
    private final CreatorQuestionService creatorQuestionService;

    @PostMapping
    public DefaultResponse register(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody RegisterSelfMadeQuestionRequest request) {
        creatorQuestionService.register(userPrincipal.getUser().getUid(), request.toModel());
        return DefaultResponse.success();
    }

    @PatchMapping("/{questionId}")
    public DefaultResponse modify(
        @AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Long questionId, @RequestBody ModifySelfMadeQuestionRequest request) {
        creatorQuestionService.modify(userPrincipal.getUser().getUid(), questionId, request.toModel());
        return DefaultResponse.success();
    }
}
