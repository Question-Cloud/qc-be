package com.eager.questioncloud.application.api.post.controller

import com.eager.questioncloud.application.api.post.dto.ModifyPostRequest
import com.eager.questioncloud.application.api.post.dto.RegisterPostRequest
import com.eager.questioncloud.application.api.post.service.PostService
import com.eager.questioncloud.core.domain.post.dto.PostDetail
import com.eager.questioncloud.core.domain.post.dto.PostListItem
import com.eager.questioncloud.core.domain.post.model.PostFile
import com.eager.questioncloud.core.exception.CoreException
import com.eager.questioncloud.core.exception.Error
import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document
import com.epages.restdocs.apispec.ResourceSnippetParametersBuilder
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.restdocs.request.RequestDocumentation.*
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDateTime

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@AutoConfigureRestDocs
class PostControllerDocument {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @MockBean
    private lateinit var postService: PostService

    private lateinit var samplePostListItems: List<PostListItem>
    private lateinit var samplePostDetail: PostDetail

    @BeforeEach
    fun setUp() {
        samplePostListItems = listOf(
            PostListItem(
                id = 1L,
                title = "미적분 극한 문제 질문드립니다",
                writer = "김학생",
                createdAt = LocalDateTime.of(2024, 3, 15, 14, 30)
            ),
            PostListItem(
                id = 2L,
                title = "이 문제 풀이가 맞나요?",
                writer = "이질문",
                createdAt = LocalDateTime.of(2024, 3, 10, 9, 15)
            )
        )

        val sampleFiles = listOf(
            PostFile(
                fileName = "solution.jpg",
                url = "https://example.com/files/solution.jpg"
            ),
            PostFile(
                fileName = "diagram.png",
                url = "https://example.com/files/diagram.png"
            )
        )

        samplePostDetail = PostDetail(
            id = 1L,
            questionId = 101L,
            title = "미적분 극한 문제 질문드립니다",
            content = "이 문제를 풀다가 막혔는데 도움을 받을 수 있을까요? 첨부한 이미지를 참고해 주세요.",
            files = sampleFiles,
            parentCategory = "미적분",
            childCategory = "극한",
            questionTitle = "미적분 기본 문제집",
            writer = "김학생",
            createdAt = LocalDateTime.of(2024, 3, 15, 14, 30)
        )
    }

    @Test
    fun `문제 게시판 글 목록 조회 API 테스트`() {
        // Given
        val questionId = 101L
        val totalCount = 50

        whenever(postService.countPost(any()))
            .thenReturn(totalCount)
        whenever(postService.getPostList(any(), any(), any()))
            .thenReturn(samplePostListItems)

        // When & Then
        mockMvc.perform(
            get("/api/post")
                .header("Authorization", "Bearer mock_access_token")
                .param("questionId", questionId.toString())
                .param("page", "1")
                .param("size", "10")
        )
            .andExpect(status().isOk)
            .andDo(
                document(
                    "문제 게시판 글 목록 조회",
                    resourceDetails = ResourceSnippetParametersBuilder()
                        .summary("문제 게시판 글 목록 조회")
                        .description("특정 문제에 대한 게시글 목록을 페이징하여 조회합니다.")
                        .tag("post"),
                    snippets = arrayOf(
                        queryParameters(
                            parameterWithName("questionId").description("게시글을 조회할 문제 ID"),
                            parameterWithName("page").description("페이지 번호 (1부터 시작)"),
                            parameterWithName("size").description("페이지당 항목 수")
                        ),
                        responseFields(
                            fieldWithPath("total").description("전체 게시글 수"),
                            fieldWithPath("result").description("게시글 목록"),
                            fieldWithPath("result[].id").description("게시글 ID"),
                            fieldWithPath("result[].title").description("게시글 제목"),
                            fieldWithPath("result[].writer").description("작성자 이름"),
                            fieldWithPath("result[].createdAt").description("작성일시")
                        )
                    )
                )
            )
    }

    @Test
    fun `문제 게시판 글 목록 조회 API 실패 테스트 - 권한 없음`() {
        // Given
        val questionId = 101L

        whenever(postService.getPostList(any(), any(), any()))
            .thenThrow(CoreException(Error.FORBIDDEN))

        // When & Then
        mockMvc.perform(
            get("/api/post")
                .header("Authorization", "Bearer mock_access_token")
                .param("questionId", questionId.toString())
                .param("page", "1")
                .param("size", "10")
        )
            .andExpect(status().isForbidden)
            .andDo(
                document(
                    "문제 게시판 글 목록 조회 실패 - 권한 없음",
                    resourceDetails = ResourceSnippetParametersBuilder()
                        .summary("문제 게시판 글 목록 조회")
                        .description("특정 문제에 대한 게시글 목록을 페이징하여 조회합니다.")
                        .tag("post"),
                    snippets = arrayOf(
                        queryParameters(
                            parameterWithName("questionId").description("게시글을 조회할 문제 ID"),
                            parameterWithName("page").description("페이지 번호 (1부터 시작)"),
                            parameterWithName("size").description("페이지당 항목 수")
                        ),
                        responseFields(
                            fieldWithPath("status").description("HTTP 상태 코드"),
                            fieldWithPath("message").description("에러 메시지")
                        )
                    )
                )
            )
    }

    @Test
    fun `문제 게시판 글 조회 API 테스트`() {
        // Given
        val postId = 1L

        whenever(postService.getPostDetail(any(), any()))
            .thenReturn(samplePostDetail)

        // When & Then
        mockMvc.perform(
            get("/api/post/{postId}", postId)
                .header("Authorization", "Bearer mock_access_token")
        )
            .andExpect(status().isOk)
            .andDo(
                document(
                    "문제 게시판 글 조회",
                    resourceDetails = ResourceSnippetParametersBuilder()
                        .summary("문제 게시판 글 조회")
                        .description("특정 게시글의 상세 정보를 조회합니다.")
                        .tag("post"),
                    snippets = arrayOf(
                        pathParameters(
                            parameterWithName("postId").description("조회할 게시글 ID")
                        ),
                        responseFields(
                            fieldWithPath("board").description("게시글 정보"),
                            fieldWithPath("board.id").description("게시글 ID"),
                            fieldWithPath("board.questionId").description("문제 ID"),
                            fieldWithPath("board.title").description("게시글 제목"),
                            fieldWithPath("board.content").description("게시글 내용"),
                            fieldWithPath("board.files").description("첨부파일 목록"),
                            fieldWithPath("board.files[].fileName").description("파일명"),
                            fieldWithPath("board.files[].url").description("파일 URL"),
                            fieldWithPath("board.parentCategory").description("문제 대분류"),
                            fieldWithPath("board.childCategory").description("문제 소분류"),
                            fieldWithPath("board.questionTitle").description("문제 제목"),
                            fieldWithPath("board.writer").description("작성자 이름"),
                            fieldWithPath("board.createdAt").description("작성일시")
                        )
                    )
                )
            )
    }

    @Test
    fun `문제 게시판 글 조회 API 실패 테스트 - 권한 없음`() {
        // Given
        val postId = 1L

        whenever(postService.getPostDetail(any(), any()))
            .thenThrow(CoreException(Error.FORBIDDEN))

        // When & Then
        mockMvc.perform(
            get("/api/post/{postId}", postId)
                .header("Authorization", "Bearer mock_access_token")
        )
            .andExpect(status().isForbidden)
            .andDo(
                document(
                    "문제 게시판 글 조회 실패 - 권한 없음",
                    resourceDetails = ResourceSnippetParametersBuilder()
                        .summary("문제 게시판 글 조회")
                        .description("특정 게시글의 상세 정보를 조회합니다.")
                        .tag("post"),
                    snippets = arrayOf(
                        pathParameters(
                            parameterWithName("postId").description("조회할 게시글 ID")
                        ),
                        responseFields(
                            fieldWithPath("status").description("HTTP 상태 코드"),
                            fieldWithPath("message").description("에러 메시지")
                        )
                    )
                )
            )
    }

    @Test
    fun `문제 게시판 글 조회 API 실패 테스트 - 게시글 없음`() {
        // Given
        val postId = 999L

        whenever(postService.getPostDetail(any(), any()))
            .thenThrow(CoreException(Error.NOT_FOUND))

        // When & Then
        mockMvc.perform(
            get("/api/post/{postId}", postId)
                .header("Authorization", "Bearer mock_access_token")
        )
            .andExpect(status().isNotFound)
            .andDo(
                document(
                    "문제 게시판 글 조회 실패 - 게시글 없음",
                    resourceDetails = ResourceSnippetParametersBuilder()
                        .summary("문제 게시판 글 조회")
                        .description("특정 게시글의 상세 정보를 조회합니다.")
                        .tag("post"),
                    snippets = arrayOf(
                        pathParameters(
                            parameterWithName("postId").description("조회할 게시글 ID")
                        ),
                        responseFields(
                            fieldWithPath("status").description("HTTP 상태 코드"),
                            fieldWithPath("message").description("에러 메시지")
                        )
                    )
                )
            )
    }

    @Test
    fun `문제 게시판 글 등록 API 테스트`() {
        // Given
        val registerPostRequest = RegisterPostRequest(
            questionId = 101L,
            title = "미적분 극한 문제 질문드립니다",
            content = "이 문제를 풀다가 막혔는데 도움을 받을 수 있을까요?",
            files = listOf(
                PostFile(
                    fileName = "problem.jpg",
                    url = "https://example.com/files/problem.jpg"
                )
            )
        )

        // When & Then
        mockMvc.perform(
            post("/api/post")
                .header("Authorization", "Bearer mock_access_token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerPostRequest))
        )
            .andExpect(status().isOk)
            .andDo(
                document(
                    "문제 게시판 글 등록",
                    resourceDetails = ResourceSnippetParametersBuilder()
                        .summary("문제 게시판 글 등록")
                        .description("문제에 대한 새로운 게시글을 등록합니다.")
                        .tag("post"),
                    snippets = arrayOf(
                        requestFields(
                            fieldWithPath("questionId").description("게시글을 등록할 문제 ID"),
                            fieldWithPath("title").description("게시글 제목"),
                            fieldWithPath("content").description("게시글 내용"),
                            fieldWithPath("files").description("첨부파일 목록 (선택사항)"),
                            fieldWithPath("files[].fileName").description("파일명").optional(),
                            fieldWithPath("files[].url").description("파일 URL").optional()
                        ),
                        responseFields(
                            fieldWithPath("success").description("요청 성공 여부")
                        )
                    )
                )
            )
    }

    @Test
    fun `문제 게시판 글 등록 API 테스트 - 파일 없이`() {
        // Given
        val registerPostRequest = RegisterPostRequest(
            questionId = 101L,
            title = "문제 풀이 방법 질문",
            content = "이 문제는 어떻게 접근해야 할까요?",
            files = emptyList()
        )

        // When & Then
        mockMvc.perform(
            post("/api/post")
                .header("Authorization", "Bearer mock_access_token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerPostRequest))
        )
            .andExpect(status().isOk)
            .andDo(
                document(
                    "문제 게시판 글 등록 - 파일 없이",
                    resourceDetails = ResourceSnippetParametersBuilder()
                        .summary("문제 게시판 글 등록")
                        .description("문제에 대한 새로운 게시글을 등록합니다.")
                        .tag("post"),
                    snippets = arrayOf(
                        requestFields(
                            fieldWithPath("questionId").description("게시글을 등록할 문제 ID"),
                            fieldWithPath("title").description("게시글 제목"),
                            fieldWithPath("content").description("게시글 내용"),
                            fieldWithPath("files").description("첨부파일 목록 (빈 배열)")
                        ),
                        responseFields(
                            fieldWithPath("success").description("요청 성공 여부")
                        )
                    )
                )
            )
    }

    @Test
    fun `문제 게시판 글 등록 API 실패 테스트 - 권한 없음`() {
        // Given
        val registerPostRequest = RegisterPostRequest(
            questionId = 101L,
            title = "미적분 극한 문제 질문드립니다",
            content = "이 문제를 풀다가 막혔는데 도움을 받을 수 있을까요?",
            files = emptyList()
        )

        whenever(postService.register(any()))
            .thenThrow(CoreException(Error.FORBIDDEN))

        // When & Then
        mockMvc.perform(
            post("/api/post")
                .header("Authorization", "Bearer mock_access_token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerPostRequest))
        )
            .andExpect(status().isForbidden)
            .andDo(
                document(
                    "문제 게시판 글 등록 실패 - 권한 없음",
                    resourceDetails = ResourceSnippetParametersBuilder()
                        .summary("문제 게시판 글 등록")
                        .description("문제에 대한 새로운 게시글을 등록합니다.")
                        .tag("post"),
                    snippets = arrayOf(
                        requestFields(
                            fieldWithPath("questionId").description("게시글을 등록할 문제 ID"),
                            fieldWithPath("title").description("게시글 제목"),
                            fieldWithPath("content").description("게시글 내용"),
                            fieldWithPath("files").description("첨부파일 목록 (빈 배열)")
                        ),
                        responseFields(
                            fieldWithPath("status").description("HTTP 상태 코드"),
                            fieldWithPath("message").description("에러 메시지")
                        )
                    )
                )
            )
    }

    @Test
    fun `문제 게시판 글 수정 API 테스트`() {
        // Given
        val postId = 1L
        val modifyPostRequest = ModifyPostRequest(
            title = "수정된 게시글 제목",
            content = "수정된 게시글 내용입니다.",
            files = listOf(
                PostFile(
                    fileName = "updated_file.jpg",
                    url = "https://example.com/files/updated_file.jpg"
                )
            )
        )

        // When & Then
        mockMvc.perform(
            patch("/api/post/{postId}", postId)
                .header("Authorization", "Bearer mock_access_token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(modifyPostRequest))
        )
            .andExpect(status().isOk)
            .andDo(
                document(
                    "문제 게시판 글 수정",
                    resourceDetails = ResourceSnippetParametersBuilder()
                        .summary("문제 게시판 글 수정")
                        .description("내가 작성한 게시글을 수정합니다.")
                        .tag("post"),
                    snippets = arrayOf(
                        pathParameters(
                            parameterWithName("postId").description("수정할 게시글 ID")
                        ),
                        requestFields(
                            fieldWithPath("title").description("수정할 게시글 제목"),
                            fieldWithPath("content").description("수정할 게시글 내용"),
                            fieldWithPath("files").description("첨부파일 목록 (선택사항)"),
                            fieldWithPath("files[].fileName").description("파일명").optional(),
                            fieldWithPath("files[].url").description("파일 URL").optional()
                        ),
                        responseFields(
                            fieldWithPath("success").description("요청 성공 여부")
                        )
                    )
                )
            )
    }

    @Test
    fun `문제 게시판 글 수정 API 실패 테스트 - 게시글 없음`() {
        // Given
        val postId = 999L
        val modifyPostRequest = ModifyPostRequest(
            title = "수정된 게시글 제목",
            content = "수정된 게시글 내용입니다.",
            files = emptyList()
        )

        whenever(postService.modify(any(), any(), any()))
            .thenThrow(CoreException(Error.NOT_FOUND))

        // When & Then
        mockMvc.perform(
            patch("/api/post/{postId}", postId)
                .header("Authorization", "Bearer mock_access_token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(modifyPostRequest))
        )
            .andExpect(status().isNotFound)
            .andDo(
                document(
                    "문제 게시판 글 수정 실패 - 게시글 없음",
                    resourceDetails = ResourceSnippetParametersBuilder()
                        .summary("문제 게시판 글 수정")
                        .description("내가 작성한 게시글을 수정합니다.")
                        .tag("post"),
                    snippets = arrayOf(
                        pathParameters(
                            parameterWithName("postId").description("수정할 게시글 ID")
                        ),
                        requestFields(
                            fieldWithPath("title").description("수정할 게시글 제목"),
                            fieldWithPath("content").description("수정할 게시글 내용"),
                            fieldWithPath("files").description("첨부파일 목록 (빈 배열)")
                        ),
                        responseFields(
                            fieldWithPath("status").description("HTTP 상태 코드"),
                            fieldWithPath("message").description("에러 메시지")
                        )
                    )
                )
            )
    }

    @Test
    fun `문제 게시판 글 삭제 API 테스트`() {
        // Given
        val postId = 1L

        // When & Then
        mockMvc.perform(
            delete("/api/post/{postId}", postId)
                .header("Authorization", "Bearer mock_access_token")
        )
            .andExpect(status().isOk)
            .andDo(
                document(
                    "문제 게시판 글 삭제",
                    resourceDetails = ResourceSnippetParametersBuilder()
                        .summary("문제 게시판 글 삭제")
                        .description("내가 작성한 게시글을 삭제합니다.")
                        .tag("post"),
                    snippets = arrayOf(
                        pathParameters(
                            parameterWithName("postId").description("삭제할 게시글 ID")
                        ),
                        responseFields(
                            fieldWithPath("success").description("요청 성공 여부")
                        )
                    )
                )
            )
    }

    @Test
    fun `문제 게시판 글 삭제 API 실패 테스트 - 게시글 없음`() {
        // Given
        val postId = 999L

        whenever(postService.delete(any(), any()))
            .thenThrow(CoreException(Error.NOT_FOUND))

        // When & Then
        mockMvc.perform(
            delete("/api/post/{postId}", postId)
                .header("Authorization", "Bearer mock_access_token")
        )
            .andExpect(status().isNotFound)
            .andDo(
                document(
                    "문제 게시판 글 삭제 실패 - 게시글 없음",
                    resourceDetails = ResourceSnippetParametersBuilder()
                        .summary("문제 게시판 글 삭제")
                        .description("내가 작성한 게시글을 삭제합니다.")
                        .tag("post"),
                    snippets = arrayOf(
                        pathParameters(
                            parameterWithName("postId").description("삭제할 게시글 ID")
                        ),
                        responseFields(
                            fieldWithPath("status").description("HTTP 상태 코드"),
                            fieldWithPath("message").description("에러 메시지")
                        )
                    )
                )
            )
    }
}
