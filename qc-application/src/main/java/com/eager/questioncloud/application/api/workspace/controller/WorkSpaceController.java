package com.eager.questioncloud.application.api.workspace.controller;

import com.eager.questioncloud.application.api.common.DefaultResponse;
import com.eager.questioncloud.application.api.common.PagingResponse;
import com.eager.questioncloud.application.api.creator.dto.RegisterCreatorControllerRequest;
import com.eager.questioncloud.application.api.creator.dto.RegisterCreatorControllerResponse.RegisterCreatorResponse;
import com.eager.questioncloud.application.api.workspace.dto.WorkSpaceControllerRequest;
import com.eager.questioncloud.application.api.workspace.dto.WorkSpaceControllerResponse.CreatorProfileResponse;
import com.eager.questioncloud.application.api.workspace.dto.WorkSpaceControllerResponse.QuestionContentResponse;
import com.eager.questioncloud.application.api.workspace.service.CreatorPostService;
import com.eager.questioncloud.application.api.workspace.service.CreatorProfileService;
import com.eager.questioncloud.application.api.workspace.service.CreatorQuestionService;
import com.eager.questioncloud.application.api.workspace.service.CreatorRegisterService;
import com.eager.questioncloud.application.security.UserPrincipal;
import com.eager.questioncloud.core.common.PagingInformation;
import com.eager.questioncloud.core.domain.creator.model.Creator;
import com.eager.questioncloud.core.domain.creator.model.CreatorProfile;
import com.eager.questioncloud.core.domain.post.dto.PostListItem;
import com.eager.questioncloud.core.domain.question.dto.QuestionInformation;
import com.eager.questioncloud.core.domain.question.model.QuestionContent;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
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
    private final CreatorProfileService creatorProfileService;
    private final CreatorPostService creatorPostService;
    private final CreatorQuestionService creatorQuestionService;
    private final CreatorRegisterService creatorRegisterService;

    @PostMapping("/register")
    @PreAuthorize("hasAnyRole('ROLE_NormalUser')")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청 성공")
    })
    @Operation(operationId = "크리에이터 등록 신청", summary = "크리에이터 등록 신청", tags = {"workspace"}, description = "크리에이터 등록 신청")
    public RegisterCreatorResponse registerCreator(
        @AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody @Valid RegisterCreatorControllerRequest.RegisterCreatorRequest request) {
        Creator creator = creatorRegisterService.register(
            userPrincipal.getUser(), new CreatorProfile(request.getMainSubject(), request.getIntroduction()));
        return new RegisterCreatorResponse(creator.getId());
    }

    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('ROLE_CreatorUser')")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청 성공")
    })
    @Operation(operationId = "크리에이터 정보 조회 (나)", summary = "크리에이터 정보 조회 (나)", tags = {"workspace"}, description = "크리에이터 정보 조회 (나)")
    public CreatorProfileResponse getMyCreatorInformation(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        CreatorProfile profile = userPrincipal.getCreator().getCreatorProfile();
        return new CreatorProfileResponse(profile);
    }

    @PatchMapping("/me")
    @PreAuthorize("hasAnyRole('ROLE_CreatorUser')")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청 성공")
    })
    @Operation(operationId = "크리에이터 정보 수정", summary = "크리에이터 정보 수정", tags = {"workspace"}, description = "크리에이터 정보 수정")
    public DefaultResponse updateMyCreatorInformation(
        @AuthenticationPrincipal UserPrincipal userPrincipal,
        @RequestBody @Valid WorkSpaceControllerRequest.UpdateCreatorProfileRequest request) {
        creatorProfileService.updateCreatorProfile(
            userPrincipal.getCreator(),
            new CreatorProfile(request.getMainSubject(), request.getIntroduction()));
        return DefaultResponse.success();
    }

    @GetMapping("/question")
    @PreAuthorize("hasAnyRole('ROLE_CreatorUser')")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청 성공")
    })
    @Operation(operationId = "나의 자작 문제 목록 조회", summary = "나의 자작 문제 목록 조회", tags = {"workspace"}, description = "나의 자작 문제 목록 조회")
    @Parameter(name = "size", description = "paging size", schema = @Schema(type = "integer"))
    @Parameter(name = "page", description = "paging page", schema = @Schema(type = "integer"))
    public PagingResponse<QuestionInformation> getQuestions(
        @AuthenticationPrincipal UserPrincipal userPrincipal, PagingInformation pagingInformation) {
        int total = creatorQuestionService.countMyQuestions(userPrincipal.getCreator().getId());
        List<QuestionInformation> questions = creatorQuestionService.getMyQuestions(userPrincipal.getCreator().getId(), pagingInformation);
        return new PagingResponse<>(total, questions);
    }

    @GetMapping("/question/{questionId}")
    @PreAuthorize("hasAnyRole('ROLE_CreatorUser')")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청 성공")
    })
    @Operation(operationId = "나의 자작 문제 상세 조회", summary = "나의 자작 문제 상세 조회", tags = {"workspace"}, description = "나의 자작 문제 상세 조회")
    public QuestionContentResponse getQuestion(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Long questionId) {
        QuestionContent questionContent = creatorQuestionService.getMyQuestionContent(userPrincipal.getCreator().getId(), questionId);
        return new QuestionContentResponse(questionContent);
    }

    @PostMapping("/question")
    @PreAuthorize("hasAnyRole('ROLE_CreatorUser')")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청 성공")
    })
    @Operation(operationId = "나의 자작 문제 등록", summary = "나의 자작 문제 등록", tags = {"workspace"}, description = "나의 자작 문제 등록")
    public DefaultResponse register(@AuthenticationPrincipal UserPrincipal userPrincipal,
        @RequestBody @Valid WorkSpaceControllerRequest.RegisterQuestionRequest request) {
        creatorQuestionService.registerQuestion(userPrincipal.getCreator().getId(), request.toQuestionContent());
        return DefaultResponse.success();
    }

    @PatchMapping("/question/{questionId}")
    @PreAuthorize("hasAnyRole('ROLE_CreatorUser')")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청 성공")
    })
    @Operation(operationId = "나의 자작 문제 수정", summary = "나의 자작 문제 수정", tags = {"workspace"}, description = "나의 자작 문제 수정")
    public DefaultResponse modify(
        @AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Long questionId,
        @RequestBody @Valid WorkSpaceControllerRequest.ModifyQuestionRequest request) {
        creatorQuestionService.modifyQuestion(userPrincipal.getCreator().getId(), questionId, request.toQuestionContent());
        return DefaultResponse.success();
    }

    @DeleteMapping("/question/{questionId}")
    @PreAuthorize("hasAnyRole('ROLE_CreatorUser')")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청 성공")
    })
    @Operation(operationId = "나의 자작 문제 삭제", summary = "나의 자작 문제 삭제", tags = {"workspace"}, description = "나의 자작 문제 삭제")
    public DefaultResponse delete(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Long questionId) {
        creatorQuestionService.deleteQuestion(userPrincipal.getCreator().getId(), questionId);
        return DefaultResponse.success();
    }

    @GetMapping("/board")
    @PreAuthorize("hasAnyRole('ROLE_CreatorUser')")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청 성공")
    })
    @Operation(operationId = "나의 자작 문제 게시판 목록 조회", summary = "나의 자작 문제 게시판 목록 조회", tags = {"workspace"},
        description = """
                나의 자작 문제 게시판에 등록된 게시글들을 통합 조회 합니다.
            """)
    public PagingResponse<PostListItem> creatorQuestionBoardList(
        @AuthenticationPrincipal UserPrincipal userPrincipal, PagingInformation pagingInformation) {
        int total = creatorPostService.countCreatorPost(userPrincipal.getCreator().getId());
        List<PostListItem> boards = creatorPostService.getCreatorPosts(
            userPrincipal.getCreator().getId(),
            pagingInformation);
        return new PagingResponse<>(total, boards);
    }
}
