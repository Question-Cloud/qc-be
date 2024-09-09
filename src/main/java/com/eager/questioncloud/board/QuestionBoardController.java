package com.eager.questioncloud.board;

import com.eager.questioncloud.board.QuestionBoardDto.QuestionBoardDetail;
import com.eager.questioncloud.board.QuestionBoardDto.QuestionBoardListItem;
import com.eager.questioncloud.board.Request.ModifyQuestionBoardRequest;
import com.eager.questioncloud.board.Request.RegisterQuestionBoardRequest;
import com.eager.questioncloud.board.Response.QuestionBoardResponse;
import com.eager.questioncloud.common.DefaultResponse;
import com.eager.questioncloud.common.PagingResponse;
import com.eager.questioncloud.security.UserPrincipal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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

    @PatchMapping("/{boardId}")
    public DefaultResponse modify(
        @AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Long boardId, @RequestBody ModifyQuestionBoardRequest request) {
        questionBoardService.modify(boardId, userPrincipal.getUser().getUid(), request.getTitle(), request.getContent(), request.getFiles());
        return DefaultResponse.success();
    }

    @GetMapping("/question/{questionId}/{boardId}")
    public QuestionBoardResponse getQuestionBoard(
        @AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Long questionId, @PathVariable Long boardId) {
        QuestionBoardDetail board = questionBoardService.getQuestionBoardDetail(userPrincipal.getUser().getUid(), questionId, boardId);
        return new QuestionBoardResponse(board);
    }

    @GetMapping("/question/{questionId}")
    public PagingResponse<QuestionBoardListItem> getQuestionBoards(
        @AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Long questionId, Pageable pageable) {
        int total = questionBoardService.countQuestionBoard(questionId);
        List<QuestionBoardListItem> boards = questionBoardService.getQuestionBoardList(userPrincipal.getUser().getUid(), questionId, pageable);
        return new PagingResponse<>(total, boards);
    }

    @PostMapping("/question/{questionId}")
    public DefaultResponse register(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Long questionId,
        @RequestBody RegisterQuestionBoardRequest request) {
        questionBoardService.register(
            QuestionBoard.create(
                questionId,
                userPrincipal.getUser().getUid(),
                request.getTitle(),
                request.getContent(),
                request.getFiles())
        );
        return DefaultResponse.success();
    }
}
