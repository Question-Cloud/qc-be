package com.eager.questioncloud.application.api.feed.library.controller

import com.eager.questioncloud.application.api.feed.library.service.FeedLibraryService
import com.eager.questioncloud.core.domain.question.enums.QuestionLevel
import com.eager.questioncloud.core.domain.userquestion.dto.UserQuestionContent
import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document
import com.epages.restdocs.apispec.ResourceSnippetParametersBuilder
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.request.RequestDocumentation.queryParameters
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@AutoConfigureRestDocs
class FeedLibraryControllerDocument {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var feedLibraryService: FeedLibraryService

    private lateinit var sampleUserQuestions: List<UserQuestionContent>

    @BeforeEach
    fun setUp() {
        sampleUserQuestions = listOf(
            UserQuestionContent(
                questionId = 1L,
                title = "수학 기본 문제 1",
                parentCategory = "수학",
                childCategory = "대수",
                thumbnail = "https://example.com/thumbnail1.jpg",
                creatorName = "김수학",
                questionLevel = QuestionLevel.LEVEL1,
                fileUrl = "https://example.com/question1.pdf",
                explanationUrl = "https://example.com/explanation1.pdf"
            ),
            UserQuestionContent(
                questionId = 2L,
                title = "물리 역학 문제 1",
                parentCategory = "물리",
                childCategory = "역학",
                thumbnail = "https://example.com/thumbnail2.jpg",
                creatorName = "이물리",
                questionLevel = QuestionLevel.LEVEL3,
                fileUrl = "https://example.com/question2.pdf",
                explanationUrl = "https://example.com/explanation2.pdf"
            )
        )
    }

    @Test
    fun `나의 문제 목록 조회 API 테스트`() {
        // Given
        val totalCount = 10

        whenever(feedLibraryService.countUserQuestions(any()))
            .thenReturn(totalCount)
        whenever(feedLibraryService.getUserQuestions(any()))
            .thenReturn(sampleUserQuestions)

        // When & Then
        mockMvc.perform(
            get("/api/feed/library")
                .header("Authorization", "Bearer mock_access_token")
                .param("page", "1")
                .param("size", "10")
                .param("sort", "Latest")
        )
            .andExpect(status().isOk)
            .andDo(
                document(
                    "나의 문제 목록 조회",
                    resourceDetails = ResourceSnippetParametersBuilder()
                        .summary("나의 문제 목록 조회")
                        .description("사용자가 보유한 문제 목록을 필터링 및 페이징하여 조회합니다.")
                        .tag("library"),
                    snippets = arrayOf(
                        queryParameters(
                            parameterWithName("page").description("페이지 번호 (1부터 시작)"),
                            parameterWithName("size").description("페이지당 항목 수"),
                            parameterWithName("sort").description("정렬 방식 (Popularity, Rate, Latest, LEVEL)"),
                            parameterWithName("categories").description("카테고리 ID 목록 (콤마로 구분)").optional(),
                            parameterWithName("levels").description("문제 난이도 목록 (LEVEL1, LEVEL2, LEVEL3, LEVEL4, LEVEL5, LEVEL6)")
                                .optional(),
                            parameterWithName("questionType").description("문제 타입").optional(),
                            parameterWithName("creatorId").description("크리에이터 ID").optional()
                        ),
                        responseFields(
                            fieldWithPath("total").description("전체 결과 수"),
                            fieldWithPath("result").description("문제 목록"),
                            fieldWithPath("result[].questionId").description("문제 ID"),
                            fieldWithPath("result[].title").description("문제 제목"),
                            fieldWithPath("result[].parentCategory").description("상위 카테고리"),
                            fieldWithPath("result[].childCategory").description("하위 카테고리"),
                            fieldWithPath("result[].thumbnail").description("썸네일 이미지 URL"),
                            fieldWithPath("result[].creatorName").description("크리에이터 이름"),
                            fieldWithPath("result[].questionLevel").description("문제 난이도"),
                            fieldWithPath("result[].fileUrl").description("문제 파일 URL"),
                            fieldWithPath("result[].explanationUrl").description("해설 파일 URL")
                        )
                    )
                )
            )
    }

    @Test
    fun `나의 문제 목록 조회 API 테스트 - 필터링 적용`() {
        // Given
        val totalCount = 5

        whenever(feedLibraryService.countUserQuestions(any()))
            .thenReturn(totalCount)
        whenever(feedLibraryService.getUserQuestions(any()))
            .thenReturn(sampleUserQuestions.take(1))

        // When & Then
        mockMvc.perform(
            get("/api/feed/library")
                .header("Authorization", "Bearer mock_access_token")
                .param("page", "1")
                .param("size", "10")
                .param("sort", "Popularity")
                .param("levels", "LEVEL1,LEVEL2")
                .param("categories", "1,2")
        )
            .andExpect(status().isOk)
            .andDo(
                document(
                    "나의 문제 목록 조회 - 필터링 적용",
                    resourceDetails = ResourceSnippetParametersBuilder()
                        .summary("나의 문제 목록 조회")
                        .description("사용자가 보유한 문제 목록을 필터링 및 페이징하여 조회합니다.")
                        .tag("library"),
                    snippets = arrayOf(
                        queryParameters(
                            parameterWithName("page").description("페이지 번호 (1부터 시작)"),
                            parameterWithName("size").description("페이지당 항목 수"),
                            parameterWithName("sort").description("정렬 방식 (Popularity, Rate, Latest, LEVEL)"),
                            parameterWithName("levels").description("문제 난이도 목록 (LEVEL1, LEVEL2, LEVEL3, LEVEL4, LEVEL5, LEVEL6)"),
                            parameterWithName("categories").description("카테고리 ID 목록 (콤마로 구분)")
                        ),
                        responseFields(
                            fieldWithPath("total").description("전체 결과 수"),
                            fieldWithPath("result").description("문제 목록"),
                            fieldWithPath("result[].questionId").description("문제 ID"),
                            fieldWithPath("result[].title").description("문제 제목"),
                            fieldWithPath("result[].parentCategory").description("상위 카테고리"),
                            fieldWithPath("result[].childCategory").description("하위 카테고리"),
                            fieldWithPath("result[].thumbnail").description("썸네일 이미지 URL"),
                            fieldWithPath("result[].creatorName").description("크리에이터 이름"),
                            fieldWithPath("result[].questionLevel").description("문제 난이도"),
                            fieldWithPath("result[].fileUrl").description("문제 파일 URL"),
                            fieldWithPath("result[].explanationUrl").description("해설 파일 URL")
                        )
                    )
                )
            )
    }
}
