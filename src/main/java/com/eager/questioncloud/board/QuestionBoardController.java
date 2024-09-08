package com.eager.questioncloud.board;

import com.eager.questioncloud.board.Request.RegisterQuestionBoardRequest;
import com.eager.questioncloud.common.DefaultResponse;
import com.eager.questioncloud.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/board")
@RequiredArgsConstructor
public class QuestionBoardController {
    private final QuestionBoardService questionBoardService;

    @PostMapping("/question/{questionId}")
    public DefaultResponse register(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Long questionId,
        @RequestBody RegisterQuestionBoardRequest request) {
        questionBoardService.register(
            QuestionBoard.create(
                questionId,
                userPrincipal.getUser().getUid(),
                request.getTitle(),
                request.getComment(),
                request.getFiles())
        );
        return DefaultResponse.success();
    }
}
