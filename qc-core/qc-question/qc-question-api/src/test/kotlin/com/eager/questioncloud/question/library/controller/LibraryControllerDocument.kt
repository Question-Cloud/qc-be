package com.eager.questioncloud.question.library.controller

import com.eager.questioncloud.question.dto.UserQuestionContent
import com.eager.questioncloud.question.enums.QuestionLevel
import com.eager.questioncloud.question.library.dto.ContentCreator
import com.eager.questioncloud.question.library.dto.LibraryContent
import com.eager.questioncloud.question.library.service.LibraryService
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
class LibraryControllerDocument {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var libraryService: LibraryService

    private lateinit var sampleUserQuestionContent: List<UserQuestionContent>

    private lateinit var sampleContentCreator: ContentCreator

    @BeforeEach
    fun setUp() {
        sampleUserQuestionContent = listOf(
            UserQuestionContent(
                questionId = 1L,
                creatorId = 1L,
                title = "수학 기본 문제 1",
                parentCategory = "수학",
                childCategory = "대수",
                thumbnail = "https://example.com/thumbnail1.jpg",
                questionLevel = QuestionLevel.LEVEL1,
                fileUrl = "https://example.com/question1.pdf",
                explanationUrl = "https://example.com/explanation1.pdf"
            ),
            UserQuestionContent(
                questionId = 1L,
                creatorId = 1L,
                title = "수학 기본 문제 2",
                parentCategory = "수학",
                childCategory = "대수",
                thumbnail = "https://example.com/thumbnail1.jpg",
                questionLevel = QuestionLevel.LEVEL1,
                fileUrl = "https://example.com/question1.pdf",
                explanationUrl = "https://example.com/explanation1.pdf"
            )
        )

        sampleContentCreator = ContentCreator("creatorName", "creatorProfileImage", "Math")
    }

    @Test
    fun `나의 문제 목록 조회 API 테스트`() {
        // Given
        val totalCount = 10

        whenever(libraryService.countUserQuestions(any(), any()))
            .thenReturn(totalCount)

        whenever(libraryService.getUserQuestions(any(), any(), any()))
            .thenReturn(sampleUserQuestionContent.map {
                LibraryContent(it, sampleContentCreator)
            })

        // When & Then
        mockMvc.perform(
            get("/api/library")
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
                            parameterWithName("levels").description("문제 난이도 목록 (LEVEL1, LEVEL2, LEVEL3, LEVEL4, LEVEL5)")
                                .optional(),
                            parameterWithName("questionType").description("문제 타입").optional(),
                            parameterWithName("creatorId").description("크리에이터 ID").optional()
                        ),
                        responseFields(
                            fieldWithPath("total").description("전체 결과 수"),
                            fieldWithPath("result").description("문제 목록"),
                            fieldWithPath("result[].content.questionId").description("문제 ID"),
                            fieldWithPath("result[].content.creatorId").description("크리에이터 ID"),
                            fieldWithPath("result[].content.title").description("문제 제목"),
                            fieldWithPath("result[].content.parentCategory").description("상위 카테고리"),
                            fieldWithPath("result[].content.childCategory").description("하위 카테고리"),
                            fieldWithPath("result[].content.thumbnail").description("썸네일 이미지 URL"),
                            fieldWithPath("result[].content.questionLevel").description("문제 난이도"),
                            fieldWithPath("result[].content.fileUrl").description("문제 파일 URL"),
                            fieldWithPath("result[].content.explanationUrl").description("해설 파일 URL"),
                            fieldWithPath("result[].creator.name").description("크리에이터 이름"),
                            fieldWithPath("result[].creator.profileImage").description("크리에이터 프로필 이미지"),
                            fieldWithPath("result[].creator.mainSubject").description("크리에이터 주요 과목"),
                        )
                    )
                )
            )
    }
}
