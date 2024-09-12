package com.eager.questioncloud.workspace;

import com.eager.questioncloud.board.QuestionBoardDto.QuestionBoardListItem;
import com.eager.questioncloud.common.DefaultResponse;
import com.eager.questioncloud.common.PagingResponse;
import com.eager.questioncloud.question.QuestionContent;
import com.eager.questioncloud.question.QuestionDto.QuestionInformation;
import com.eager.questioncloud.question.Request.ModifySelfMadeQuestionRequest;
import com.eager.questioncloud.question.Request.RegisterSelfMadeQuestionRequest;
import com.eager.questioncloud.question.Response.QuestionContentResponse;
import com.eager.questioncloud.security.UserPrincipal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/workspace")
@RequiredArgsConstructor
public class WorkSpaceController {
    private final WorkSpaceQuestionService workSpaceQuestionService;
    private final WorkSpaceBoardService workSpaceBoardService;

    @GetMapping("/question")
    public PagingResponse<QuestionInformation> getQuestions(@AuthenticationPrincipal UserPrincipal userPrincipal, Pageable pageable) {
        int total = workSpaceQuestionService.count(userPrincipal.getUser().getUid());
        List<QuestionInformation> questions = workSpaceQuestionService.getQuestions(userPrincipal.getUser().getUid(), pageable);
        return new PagingResponse<>(total, questions);
    }

    @GetMapping("/question/{questionId}")
    public QuestionContentResponse getQuestion(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Long questionId) {
        QuestionContent questionContent = workSpaceQuestionService.getQuestionContent(userPrincipal.getUser().getUid(), questionId);
        return new QuestionContentResponse(questionContent);
    }

    @PostMapping("/question")
    public DefaultResponse register(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody RegisterSelfMadeQuestionRequest request) {
        workSpaceQuestionService.register(userPrincipal.getUser().getUid(), request.toModel());
        return DefaultResponse.success();
    }

    @PatchMapping("/question/{questionId}")
    public DefaultResponse modify(
        @AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Long questionId, @RequestBody ModifySelfMadeQuestionRequest request) {
        workSpaceQuestionService.modify(userPrincipal.getUser().getUid(), questionId, request.toModel());
        return DefaultResponse.success();
    }

    @DeleteMapping("/question/{questionId}")
    public DefaultResponse delete(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Long questionId) {
        workSpaceQuestionService.delete(userPrincipal.getUser().getUid(), questionId);
        return DefaultResponse.success();
    }

    @GetMapping("/board")
    public PagingResponse<QuestionBoardListItem> creatorQuestionBoardList(@AuthenticationPrincipal UserPrincipal userPrincipal, Pageable pageable) {
        int total = workSpaceBoardService.countCreatorQuestionBoardList(userPrincipal.getUser().getUid());
        List<QuestionBoardListItem> boards = workSpaceBoardService.getCreatorQuestionBoardList(userPrincipal.getUser().getUid(), pageable);
        return new PagingResponse<>(total, boards);
    }
}
