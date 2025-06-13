package com.eager.questioncloud.application.api.hub.review.controller

import com.eager.questioncloud.application.api.hub.review.dto.ModifyQuestionReviewRequest
import com.eager.questioncloud.application.api.hub.review.dto.MyQuestionReview
import com.eager.questioncloud.application.api.hub.review.dto.RegisterQuestionReviewRequest
import com.eager.questioncloud.application.api.hub.review.service.HubReviewService
import com.eager.questioncloud.core.domain.review.dto.QuestionReviewDetail
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
class HubReviewControllerDocument {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @MockBean
    private lateinit var hubReviewService: HubReviewService

    private lateinit var sampleQuestionReviews: List<QuestionReviewDetail>
    private lateinit var sampleMyReview: MyQuestionReview

    @BeforeEach
    fun setUp() {
        sampleQuestionReviews = listOf(
            QuestionReviewDetail(
                id = 1L,
                reviewerName = "김리뷰",
                isCreator = false,
                isWriter = false,
                reviewCount = 15,
                rateAverage = 4.7,
                rate = 5,
                comment = "정말 좋은 문제집입니다. 이해하기 쉽게 설명되어 있어요.",
                createdAt = LocalDateTime.of(2024, 3, 15, 14, 30)
            ),
            QuestionReviewDetail(
                id = 2L,
                reviewerName = "이학생",
                isCreator = false,
                isWriter = true,
                reviewCount = 8,
                rateAverage = 4.2,
                rate = 4,
                comment = "문제 난이도가 적당하고 해설이 자세해서 도움이 많이 되었습니다.",
                createdAt = LocalDateTime.of(2024, 3, 10, 9, 15)
            )
        )

        sampleMyReview = MyQuestionReview(
            id = 3L,
            rate = 5,
            comment = "정말 유용한 문제집이었습니다!"
        )
    }

    @Test
    fun `문제 리뷰 목록 조회 API 테스트`() {
        // Given
        val questionId = 101L
        val totalCount = 50

        whenever(hubReviewService.count(questionId))
            .thenReturn(totalCount)
        whenever(hubReviewService.getQuestionReviewDetails(any(), any(), any()))
            .thenReturn(sampleQuestionReviews)

        // When & Then
        mockMvc.perform(
            get("/api/hub/question/review")
                .header("Authorization", "Bearer mock_access_token")
                .param("questionId", questionId.toString())
                .param("page", "1")
                .param("size", "10")
        )
            .andExpect(status().isOk)
            .andDo(
                document(
                    "문제 리뷰 목록 조회",
                    resourceDetails = ResourceSnippetParametersBuilder()
                        .summary("문제 리뷰 목록 조회")
                        .description("특정 문제에 대한 리뷰 목록을 페이징하여 조회합니다.")
                        .tag("hub-review"),
                    snippets = arrayOf(
                        queryParameters(
                            parameterWithName("questionId").description("리뷰를 조회할 문제 ID"),
                            parameterWithName("page").description("페이지 번호 (1부터 시작)"),
                            parameterWithName("size").description("페이지당 항목 수")
                        ),
                        responseFields(
                            fieldWithPath("total").description("전체 리뷰 수"),
                            fieldWithPath("result").description("리뷰 목록"),
                            fieldWithPath("result[].id").description("리뷰 ID"),
                            fieldWithPath("result[].name").description("작성자 이름"),
                            fieldWithPath("result[].isCreator").description("크리에이터 여부"),
                            fieldWithPath("result[].isWriter").description("내가 작성한 리뷰 여부"),
                            fieldWithPath("result[].reviewCount").description("작성자의 총 리뷰 수"),
                            fieldWithPath("result[].rateAverage").description("작성자의 평균 평점"),
                            fieldWithPath("result[].rate").description("이 리뷰의 평점 (1-5)"),
                            fieldWithPath("result[].comment").description("리뷰 내용"),
                            fieldWithPath("result[].createdAt").description("작성일시")
                        )
                    )
                )
            )
    }

    @Test
    fun `내가 작성한 리뷰 조회 API 테스트`() {
        // Given
        val questionId = 101L

        whenever(hubReviewService.getMyQuestionReview(any(), any()))
            .thenReturn(sampleMyReview)

        // When & Then
        mockMvc.perform(
            get("/api/hub/question/review/me")
                .header("Authorization", "Bearer mock_access_token")
                .param("questionId", questionId.toString())
        )
            .andExpect(status().isOk)
            .andDo(
                document(
                    "내가 작성한 리뷰 조회",
                    resourceDetails = ResourceSnippetParametersBuilder()
                        .summary("내가 작성한 리뷰 조회")
                        .description("특정 문제에 대해 내가 작성한 리뷰를 조회합니다.")
                        .tag("hub-review"),
                    snippets = arrayOf(
                        queryParameters(
                            parameterWithName("questionId").description("리뷰를 조회할 문제 ID")
                        ),
                        responseFields(
                            fieldWithPath("review").description("내 리뷰 정보"),
                            fieldWithPath("review.id").description("리뷰 ID"),
                            fieldWithPath("review.rate").description("평점 (1-5)"),
                            fieldWithPath("review.comment").description("리뷰 내용")
                        )
                    )
                )
            )
    }

    @Test
    fun `내가 작성한 리뷰 조회 API 실패 테스트 - 리뷰 없음`() {
        // Given
        val questionId = 101L

        whenever(hubReviewService.getMyQuestionReview(any(), any()))
            .thenThrow(CoreException(Error.NOT_FOUND))

        // When & Then
        mockMvc.perform(
            get("/api/hub/question/review/me")
                .header("Authorization", "Bearer mock_access_token")
                .param("questionId", questionId.toString())
        )
            .andExpect(status().isNotFound)
            .andDo(
                document(
                    "내가 작성한 리뷰 조회 실패 - 리뷰 없음",
                    resourceDetails = ResourceSnippetParametersBuilder()
                        .summary("내가 작성한 리뷰 조회")
                        .description("특정 문제에 대해 내가 작성한 리뷰를 조회합니다.")
                        .tag("hub-review"),
                    snippets = arrayOf(
                        queryParameters(
                            parameterWithName("questionId").description("리뷰를 조회할 문제 ID")
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
    fun `문제 리뷰 등록 API 테스트`() {
        // Given
        val registerRequest = RegisterQuestionReviewRequest(
            questionId = 101L,
            rate = 5,
            comment = "정말 도움이 되는 문제집입니다!"
        )

        // When & Then
        mockMvc.perform(
            post("/api/hub/question/review")
                .header("Authorization", "Bearer mock_access_token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest))
        )
            .andExpect(status().isOk)
            .andDo(
                document(
                    "문제 리뷰 등록",
                    resourceDetails = ResourceSnippetParametersBuilder()
                        .summary("문제 리뷰 등록")
                        .description("문제에 대한 리뷰를 등록합니다.")
                        .tag("hub-review"),
                    snippets = arrayOf(
                        requestFields(
                            fieldWithPath("questionId").description("리뷰를 등록할 문제 ID"),
                            fieldWithPath("rate").description("평점 (1-5)"),
                            fieldWithPath("comment").description("리뷰 내용")
                        ),
                        responseFields(
                            fieldWithPath("success").description("요청 성공 여부")
                        )
                    )
                )
            )
    }


    @Test
    fun `문제 리뷰 등록 API 실패 테스트 - 이미 리뷰 작성함`() {
        // Given
        val registerRequest = RegisterQuestionReviewRequest(
            questionId = 101L,
            rate = 5,
            comment = "정말 도움이 되는 문제집입니다!"
        )

        whenever(hubReviewService.register(any()))
            .thenThrow(CoreException(Error.ALREADY_REGISTER_REVIEW))

        // When & Then
        mockMvc.perform(
            post("/api/hub/question/review")
                .header("Authorization", "Bearer mock_access_token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest))
        )
            .andExpect(status().isConflict)
            .andDo(
                document(
                    "문제 리뷰 등록 실패 - 이미 리뷰 작성함",
                    resourceDetails = ResourceSnippetParametersBuilder()
                        .summary("문제 리뷰 등록")
                        .description("문제에 대한 리뷰를 등록합니다.")
                        .tag("hub-review"),
                    snippets = arrayOf(
                        requestFields(
                            fieldWithPath("questionId").description("리뷰를 등록할 문제 ID"),
                            fieldWithPath("rate").description("평점 (1-5)"),
                            fieldWithPath("comment").description("리뷰 내용")
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
    fun `문제 리뷰 등록 API 실패 테스트 - 구매하지 않은 문제`() {
        // Given
        val registerRequest = RegisterQuestionReviewRequest(
            questionId = 101L,
            rate = 5,
            comment = "정말 도움이 되는 문제집입니다!"
        )

        whenever(hubReviewService.register(any()))
            .thenThrow(CoreException(Error.NOT_OWNED_QUESTION))

        // When & Then
        mockMvc.perform(
            post("/api/hub/question/review")
                .header("Authorization", "Bearer mock_access_token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest))
        )
            .andExpect(status().isForbidden)
            .andDo(
                document(
                    "문제 리뷰 등록 실패 - 구매하지 않은 문제",
                    resourceDetails = ResourceSnippetParametersBuilder()
                        .summary("문제 리뷰 등록")
                        .description("문제에 대한 리뷰를 등록합니다.")
                        .tag("hub-review"),
                    snippets = arrayOf(
                        requestFields(
                            fieldWithPath("questionId").description("리뷰를 등록할 문제 ID"),
                            fieldWithPath("rate").description("평점 (1-5)"),
                            fieldWithPath("comment").description("리뷰 내용")
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
    fun `문제 리뷰 수정 API 테스트`() {
        // Given
        val reviewId = 1L
        val modifyRequest = ModifyQuestionReviewRequest(
            rate = 4,
            comment = "수정된 리뷰 내용입니다."
        )

        // When & Then
        mockMvc.perform(
            patch("/api/hub/question/review/{reviewId}", reviewId)
                .header("Authorization", "Bearer mock_access_token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(modifyRequest))
        )
            .andExpect(status().isOk)
            .andDo(
                document(
                    "문제 리뷰 수정",
                    resourceDetails = ResourceSnippetParametersBuilder()
                        .summary("문제 리뷰 수정")
                        .description("기존에 작성한 리뷰를 수정합니다.")
                        .tag("hub-review"),
                    snippets = arrayOf(
                        pathParameters(
                            parameterWithName("reviewId").description("수정할 리뷰 ID")
                        ),
                        requestFields(
                            fieldWithPath("rate").description("평점 (1-5)"),
                            fieldWithPath("comment").description("리뷰 내용")
                        ),
                        responseFields(
                            fieldWithPath("success").description("요청 성공 여부")
                        )
                    )
                )
            )
    }

    @Test
    fun `문제 리뷰 수정 API 실패 테스트 - 내가 작성하지 않은 리뷰`() {
        // Given
        val reviewId = 1L
        val modifyRequest = ModifyQuestionReviewRequest(
            rate = 4,
            comment = "수정된 리뷰 내용입니다."
        )

        whenever(hubReviewService.modify(any(), any(), any(), any()))
            .thenThrow(CoreException(Error.NOT_FOUND))

        // When & Then
        mockMvc.perform(
            patch("/api/hub/question/review/{reviewId}", reviewId)
                .header("Authorization", "Bearer mock_access_token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(modifyRequest))
        )
            .andExpect(status().isNotFound)
            .andDo(
                document(
                    "문제 리뷰 수정 실패 - 내가 작성하지 않은 리뷰",
                    resourceDetails = ResourceSnippetParametersBuilder()
                        .summary("문제 리뷰 수정")
                        .description("기존에 작성한 리뷰를 수정합니다.")
                        .tag("hub-review"),
                    snippets = arrayOf(
                        pathParameters(
                            parameterWithName("reviewId").description("수정할 리뷰 ID")
                        ),
                        requestFields(
                            fieldWithPath("rate").description("평점 (1-5)"),
                            fieldWithPath("comment").description("리뷰 내용")
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
    fun `문제 리뷰 삭제 API 테스트`() {
        // Given
        val reviewId = 1L

        // When & Then
        mockMvc.perform(
            delete("/api/hub/question/review/{reviewId}", reviewId)
                .header("Authorization", "Bearer mock_access_token")
        )
            .andExpect(status().isOk)
            .andDo(
                document(
                    "문제 리뷰 삭제",
                    resourceDetails = ResourceSnippetParametersBuilder()
                        .summary("문제 리뷰 삭제")
                        .description("기존에 작성한 리뷰를 삭제합니다.")
                        .tag("hub-review"),
                    snippets = arrayOf(
                        pathParameters(
                            parameterWithName("reviewId").description("삭제할 리뷰 ID")
                        ),
                        responseFields(
                            fieldWithPath("success").description("요청 성공 여부")
                        )
                    )
                )
            )
    }

    @Test
    fun `문제 리뷰 삭제 API 실패 테스트 - 내가 작성하지 않은 리뷰`() {
        // Given
        val reviewId = 1L

        whenever(hubReviewService.delete(any(), any()))
            .thenThrow(CoreException(Error.NOT_FOUND))

        // When & Then
        mockMvc.perform(
            delete("/api/hub/question/review/{reviewId}", reviewId)
                .header("Authorization", "Bearer mock_access_token")
        )
            .andExpect(status().isNotFound)
            .andDo(
                document(
                    "문제 리뷰 삭제 실패 - 내가 작성하지 않은 리뷰",
                    resourceDetails = ResourceSnippetParametersBuilder()
                        .summary("문제 리뷰 삭제")
                        .description("기존에 작성한 리뷰를 삭제합니다.")
                        .tag("hub-review"),
                    snippets = arrayOf(
                        pathParameters(
                            parameterWithName("reviewId").description("삭제할 리뷰 ID")
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
