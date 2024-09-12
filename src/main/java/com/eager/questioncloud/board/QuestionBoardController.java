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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @DeleteMapping("/{boardId}")
    public DefaultResponse delete(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Long boardId) {
        questionBoardService.delete(boardId, userPrincipal.getUser().getUid());
        return DefaultResponse.success();
    }

    @GetMapping("/{boardId}")
    public QuestionBoardResponse getQuestionBoard(
        @AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Long boardId) {
        QuestionBoardDetail board = questionBoardService.getQuestionBoardDetail(userPrincipal.getUser().getUid(), boardId);
        return new QuestionBoardResponse(board);
    }

    @GetMapping
    public PagingResponse<QuestionBoardListItem> getQuestionBoards(
        @AuthenticationPrincipal UserPrincipal userPrincipal, @RequestParam Long questionId, Pageable pageable) {
        int total = questionBoardService.countQuestionBoard(questionId);
        List<QuestionBoardListItem> boards = questionBoardService.getQuestionBoardList(userPrincipal.getUser().getUid(), questionId, pageable);
        return new PagingResponse<>(total, boards);
    }

    @PostMapping
    public DefaultResponse register(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody RegisterQuestionBoardRequest request) {
        questionBoardService.register(
            QuestionBoard.create(
                request.getQuestionId(),
                userPrincipal.getUser().getUid(),
                request.getTitle(),
                request.getContent(),
                request.getFiles())
        );
        return DefaultResponse.success();
    }
}
