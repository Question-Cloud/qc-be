package com.eager.questioncloud.post.controller

import com.eager.questioncloud.application.security.JwtAuthenticationFilter
import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import com.eager.questioncloud.filter.FilterExceptionHandlerFilter
import com.eager.questioncloud.post.dto.AddPostCommentRequest
import com.eager.questioncloud.post.dto.ModifyPostCommentRequest
import com.eager.questioncloud.post.dto.PostCommentDetail
import com.eager.questioncloud.post.service.PostCommentService
import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document
import com.epages.restdocs.apispec.ResourceSnippetParametersBuilder
import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.kotest.core.extensions.ApplyExtension
import io.kotest.core.spec.style.FunSpec
import io.kotest.extensions.spring.SpringExtension
import io.mockk.every
import io.mockk.justRun
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.FilterType
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.restdocs.request.RequestDocumentation.*
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDateTime

@WebMvcTest(
    controllers = [PostCommentController::class],
    excludeFilters = [
        ComponentScan.Filter(
            type = FilterType.ASSIGNABLE_TYPE,
            classes = [JwtAuthenticationFilter::class]
        ),
        ComponentScan.Filter(
            type = FilterType.ASSIGNABLE_TYPE,
            classes = [FilterExceptionHandlerFilter::class]
        ),
    ]
)
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureRestDocs
@ApplyExtension(SpringExtension::class)
class PostCommentControllerDocument(
    @Autowired private val mockMvc: MockMvc,
    @Autowired private val objectMapper: ObjectMapper,
) : FunSpec() {

    @MockkBean
    private lateinit var postCommentService: PostCommentService

    private val samplePostComments = listOf(
        PostCommentDetail(
            id = 1L,
            writerName = "김댓글",
            profileImage = "https://example.com/profile1.jpg",
            comment = "정말 좋은 글이네요! 도움이 많이 되었습니다.",
            isCreator = false,
            isWriter = false,
            createdAt = LocalDateTime.of(2024, 3, 15, 14, 30)
        ),
        PostCommentDetail(
            id = 2L,
            writerName = "이학생",
            profileImage = "https://example.com/profile2.jpg",
            comment = "저도 같은 문제로 고민했는데 해결됐어요.",
            isCreator = true,
            isWriter = true,
            createdAt = LocalDateTime.of(2024, 3, 10, 9, 15)
        )
    )

    init {
        test("문제 게시글 댓글 조회 API 테스트") {
            val postId = 101L
            val totalCount = 25

            every { postCommentService.count(any()) } returns totalCount
            every { postCommentService.getPostCommentDetails(any(), any(), any()) } returns samplePostComments

            mockMvc.perform(
                get("/api/post/comment")
                    .header("Authorization", "Bearer mock_access_token")
                    .param("postId", postId.toString())
                    .param("page", "1")
                    .param("size", "10")
            )
                .andExpect(status().isOk)
                .andDo(
                    document(
                        "문제 게시글 댓글 조회",
                        resourceDetails = ResourceSnippetParametersBuilder()
                            .summary("문제 게시글 댓글 조회")
                            .description("특정 게시글의 댓글 목록을 페이징하여 조회합니다.")
                            .tag("post"),
                        snippets = arrayOf(
                            queryParameters(
                                parameterWithName("postId").description("댓글을 조회할 게시글 ID"),
                                parameterWithName("page").description("페이지 번호 (1부터 시작)"),
                                parameterWithName("size").description("페이지당 항목 수")
                            ),
                            responseFields(
                                fieldWithPath("total").description("전체 댓글 수"),
                                fieldWithPath("result").description("댓글 목록"),
                                fieldWithPath("result[].id").description("댓글 ID"),
                                fieldWithPath("result[].writerName").description("작성자 이름"),
                                fieldWithPath("result[].profileImage").description("작성자 프로필 이미지 URL"),
                                fieldWithPath("result[].comment").description("댓글 내용"),
                                fieldWithPath("result[].isCreator").description("크리에이터 여부"),
                                fieldWithPath("result[].isWriter").description("내가 작성한 댓글 여부"),
                                fieldWithPath("result[].createdAt").description("작성일시")
                            )
                        )
                    )
                )
        }

        test("문제 게시글 댓글 조회 API 실패 테스트 - 권한 없음") {
            val postId = 101L

            every { postCommentService.getPostCommentDetails(any(), any(), any()) } throws CoreException(Error.FORBIDDEN)

            mockMvc.perform(
                get("/api/post/comment")
                    .header("Authorization", "Bearer mock_access_token")
                    .param("postId", postId.toString())
                    .param("page", "1")
                    .param("size", "10")
            )
                .andExpect(status().isForbidden)
                .andDo(
                    document(
                        "문제 게시글 댓글 조회 실패 - 권한 없음",
                        resourceDetails = ResourceSnippetParametersBuilder()
                            .summary("문제 게시글 댓글 조회")
                            .description("특정 게시글의 댓글 목록을 페이징하여 조회합니다.")
                            .tag("post"),
                        snippets = arrayOf(
                            queryParameters(
                                parameterWithName("postId").description("댓글을 조회할 게시글 ID"),
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

        test("문제 게시글 댓글 작성 API 테스트") {
            val addCommentRequest = AddPostCommentRequest(
                postId = 101L,
                comment = "정말 유익한 글이네요!"
            )

            justRun { postCommentService.addPostComment(any()) }

            mockMvc.perform(
                post("/api/post/comment")
                    .header("Authorization", "Bearer mock_access_token")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(addCommentRequest))
            )
                .andExpect(status().isOk)
                .andDo(
                    document(
                        "문제 게시글 댓글 작성",
                        resourceDetails = ResourceSnippetParametersBuilder()
                            .summary("문제 게시글 댓글 작성")
                            .description("특정 게시글에 댓글을 작성합니다.")
                            .tag("post"),
                        snippets = arrayOf(
                            requestFields(
                                fieldWithPath("postId").description("댓글을 작성할 게시글 ID"),
                                fieldWithPath("comment").description("댓글 내용")
                            ),
                            responseFields(
                                fieldWithPath("success").description("요청 성공 여부")
                            )
                        )
                    )
                )
        }

        test("문제 게시글 댓글 작성 API 실패 테스트 - 권한 없음") {
            val addCommentRequest = AddPostCommentRequest(
                postId = 101L,
                comment = "정말 유익한 글이네요!"
            )

            every { postCommentService.addPostComment(any()) } throws CoreException(Error.FORBIDDEN)

            mockMvc.perform(
                post("/api/post/comment")
                    .header("Authorization", "Bearer mock_access_token")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(addCommentRequest))
            )
                .andExpect(status().isForbidden)
                .andDo(
                    document(
                        "문제 게시글 댓글 작성 실패 - 권한 없음",
                        resourceDetails = ResourceSnippetParametersBuilder()
                            .summary("문제 게시글 댓글 작성")
                            .description("특정 게시글에 댓글을 작성합니다.")
                            .tag("post"),
                        snippets = arrayOf(
                            requestFields(
                                fieldWithPath("postId").description("댓글을 작성할 게시글 ID"),
                                fieldWithPath("comment").description("댓글 내용")
                            ),
                            responseFields(
                                fieldWithPath("status").description("HTTP 상태 코드"),
                                fieldWithPath("message").description("에러 메시지")
                            )
                        )
                    )
                )
        }

        test("문제 게시글 댓글 수정 API 테스트") {
            val commentId = 1L
            val modifyCommentRequest = ModifyPostCommentRequest(
                comment = "수정된 댓글 내용입니다."
            )

            justRun { postCommentService.modifyPostComment(any()) }

            mockMvc.perform(
                patch("/api/post/comment/{commentId}", commentId)
                    .header("Authorization", "Bearer mock_access_token")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(modifyCommentRequest))
            )
                .andExpect(status().isOk)
                .andDo(
                    document(
                        "문제 게시글 댓글 수정",
                        resourceDetails = ResourceSnippetParametersBuilder()
                            .summary("문제 게시글 댓글 수정")
                            .description("내가 작성한 댓글을 수정합니다.")
                            .tag("post"),
                        snippets = arrayOf(
                            pathParameters(
                                parameterWithName("commentId").description("수정할 댓글 ID")
                            ),
                            requestFields(
                                fieldWithPath("comment").description("수정할 댓글 내용")
                            ),
                            responseFields(
                                fieldWithPath("success").description("요청 성공 여부")
                            )
                        )
                    )
                )
        }

        test("문제 게시글 댓글 수정 API 실패 테스트 - 존재하지 않는 댓글") {
            val commentId = 999L
            val modifyCommentRequest = ModifyPostCommentRequest(
                comment = "수정된 댓글 내용입니다."
            )

            every { postCommentService.modifyPostComment(any()) } throws CoreException(Error.NOT_FOUND)

            mockMvc.perform(
                patch("/api/post/comment/{commentId}", commentId)
                    .header("Authorization", "Bearer mock_access_token")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(modifyCommentRequest))
            )
                .andExpect(status().isNotFound)
                .andDo(
                    document(
                        "문제 게시글 댓글 수정 실패 - 존재하지 않는 댓글",
                        resourceDetails = ResourceSnippetParametersBuilder()
                            .summary("문제 게시글 댓글 수정")
                            .description("내가 작성한 댓글을 수정합니다.")
                            .tag("post"),
                        snippets = arrayOf(
                            pathParameters(
                                parameterWithName("commentId").description("수정할 댓글 ID")
                            ),
                            requestFields(
                                fieldWithPath("comment").description("수정할 댓글 내용")
                            ),
                            responseFields(
                                fieldWithPath("status").description("HTTP 상태 코드"),
                                fieldWithPath("message").description("에러 메시지")
                            )
                        )
                    )
                )
        }

        test("문제 게시글 댓글 삭제 API 테스트") {
            val commentId = 1L

            justRun { postCommentService.deletePostComment(any()) }

            mockMvc.perform(
                delete("/api/post/comment/{commentId}", commentId)
                    .header("Authorization", "Bearer mock_access_token")
            )
                .andExpect(status().isOk)
                .andDo(
                    document(
                        "문제 게시글 댓글 삭제",
                        resourceDetails = ResourceSnippetParametersBuilder()
                            .summary("문제 게시글 댓글 삭제")
                            .description("내가 작성한 댓글을 삭제합니다.")
                            .tag("post"),
                        snippets = arrayOf(
                            pathParameters(
                                parameterWithName("commentId").description("삭제할 댓글 ID")
                            ),
                            responseFields(
                                fieldWithPath("success").description("요청 성공 여부")
                            )
                        )
                    )
                )
        }

        test("문제 게시글 댓글 삭제 API 실패 테스트 - 존재하지 않는 댓글") {
            val commentId = 999L

            every { postCommentService.deletePostComment(any()) } throws CoreException(Error.NOT_FOUND)

            mockMvc.perform(
                delete("/api/post/comment/{commentId}", commentId)
                    .header("Authorization", "Bearer mock_access_token")
            )
                .andExpect(status().isNotFound)
                .andDo(
                    document(
                        "문제 게시글 댓글 삭제 실패 - 존재하지 않는 댓글",
                        resourceDetails = ResourceSnippetParametersBuilder()
                            .summary("문제 게시글 댓글 삭제")
                            .description("내가 작성한 댓글을 삭제합니다.")
                            .tag("post"),
                        snippets = arrayOf(
                            pathParameters(
                                parameterWithName("commentId").description("삭제할 댓글 ID")
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
}