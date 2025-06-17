package com.eager.questioncloud.store.product

import com.eager.questioncloud.api.product.dto.StoreProductDetail
import com.eager.questioncloud.api.product.service.StoreProductService
import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import com.eager.questioncloud.question.dto.QuestionCategoryGroupBySubject
import com.eager.questioncloud.question.dto.QuestionInformation
import com.eager.questioncloud.question.enums.QuestionLevel
import com.eager.questioncloud.question.enums.Subject
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
import org.springframework.restdocs.request.RequestDocumentation.*
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@AutoConfigureRestDocs
class StoreProductControllerDocument {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var storeProductService: StoreProductService

    private lateinit var sampleStoreProductDetails: List<StoreProductDetail>
    private lateinit var sampleStoreProductDetail: StoreProductDetail
    private lateinit var sampleQuestionCategories: List<QuestionCategoryGroupBySubject>

    @BeforeEach
    fun setUp() {
        sampleStoreProductDetails = listOf(
            StoreProductDetail(
                QuestionInformation(
                    id = 1L,
                    creatorId = 1L,
                    title = "수학 기본 문제집 1",
                    subject = Subject.Mathematics,
                    parentCategory = "수학",
                    childCategory = "대수",
                    thumbnail = "https://example.com/thumbnail1.jpg",
                    questionLevel = QuestionLevel.LEVEL1,
                    price = 15000,
                    rate = 4.8,
                ),
                "Creator1",
                true,
            ),
            StoreProductDetail(
                QuestionInformation(
                    id = 2L,
                    creatorId = 2L,
                    title = "물리 역학 문제집",
                    subject = Subject.Physics,
                    parentCategory = "물리",
                    childCategory = "역학",
                    thumbnail = "https://example.com/thumbnail2.jpg",
                    questionLevel = QuestionLevel.LEVEL3,
                    price = 20000,
                    rate = 4.5,
                ),
                "Creator2",
                false,
            )
        )

        sampleStoreProductDetail = StoreProductDetail(
            QuestionInformation(
                id = 3L,
                creatorId = 1L,
                title = "수학 기본 문제집 3",
                subject = Subject.Mathematics,
                parentCategory = "수학",
                childCategory = "대수",
                thumbnail = "https://example.com/thumbnail1.jpg",
                questionLevel = QuestionLevel.LEVEL1,
                price = 15000,
                rate = 4.8,
            ),
            "Creator1",
            true,
        )

        val mathSubCategories = listOf(
            QuestionCategoryGroupBySubject.SubQuestionCategory(1L, "대수"),
            QuestionCategoryGroupBySubject.SubQuestionCategory(2L, "기하")
        )

        val physicsSubCategories = listOf(
            QuestionCategoryGroupBySubject.SubQuestionCategory(3L, "역학"),
            QuestionCategoryGroupBySubject.SubQuestionCategory(4L, "전자기학")
        )

        val mathMainCategory = QuestionCategoryGroupBySubject.MainQuestionCategory(
            title = "수학",
            subject = Subject.Mathematics,
            sub = mathSubCategories
        )

        val physicsMainCategory = QuestionCategoryGroupBySubject.MainQuestionCategory(
            title = "물리",
            subject = Subject.Physics,
            sub = physicsSubCategories
        )

        sampleQuestionCategories = listOf(
            QuestionCategoryGroupBySubject(Subject.Mathematics, listOf(mathMainCategory)),
            QuestionCategoryGroupBySubject(Subject.Physics, listOf(physicsMainCategory))
        )
    }

    @Test
    fun `문제 목록 조회 API 테스트`() {
        // Given
        val totalCount = 100

        whenever(storeProductService.getTotalFiltering(any()))
            .thenReturn(totalCount)
        whenever(storeProductService.getQuestionListByFiltering(any()))
            .thenReturn(sampleStoreProductDetails)

        // When & Then
        mockMvc.perform(
            get("/api/store/product")
                .param("page", "1")
                .param("size", "10")
                .param("sort", "Latest")
        )
            .andExpect(status().isOk)
            .andDo(
                document(
                    "문제 목록 조회",
                    resourceDetails = ResourceSnippetParametersBuilder()
                        .summary("문제 목록 조회")
                        .description("필터링 조건에 따라 문제 목록을 페이징하여 조회합니다.")
                        .tag("hub-question"),
                    snippets = arrayOf(
                        queryParameters(
                            parameterWithName("page").description("페이지 번호 (1부터 시작)"),
                            parameterWithName("size").description("페이지당 항목 수"),
                            parameterWithName("sort").description("정렬 방식 (Popularity, Rate, Latest, LEVEL)"),
                            parameterWithName("categories").description("카테고리 ID 목록 (콤마로 구분)").optional(),
                            parameterWithName("levels").description("문제 난이도 목록 (LEVEL1, LEVEL2, LEVEL3, LEVEL4, LEVEL5, LEVEL6)")
                                .optional(),
                            parameterWithName("questionType").description("문제 타입").optional(),
                            parameterWithName("creatorId").description("크리에이터 ID").optional(),
                            parameterWithName("subjects").description("과목 목록 (Mathematics, Physics, Chemistry, Biology, EarthScience)")
                                .optional()
                        ),
                        responseFields(
                            fieldWithPath("total").description("전체 문제 수"),
                            fieldWithPath("result").description("문제 목록"),
                            fieldWithPath("result[].questionContent.id").description("문제 ID"),
                            fieldWithPath("result[].questionContent.creatorId").description("크리에이터 ID"),
                            fieldWithPath("result[].questionContent.title").description("문제 제목"),
                            fieldWithPath("result[].questionContent.subject").description("과목"),
                            fieldWithPath("result[].questionContent.parentCategory").description("상위 카테고리"),
                            fieldWithPath("result[].questionContent.childCategory").description("하위 카테고리"),
                            fieldWithPath("result[].questionContent.thumbnail").description("썸네일 이미지 URL"),
                            fieldWithPath("result[].questionContent.questionLevel").description("문제 난이도"),
                            fieldWithPath("result[].questionContent.price").description("문제 가격"),
                            fieldWithPath("result[].questionContent.rate").description("평균 평점"),
                            fieldWithPath("result[].creator").description("크리에이터 이름"),
                            fieldWithPath("result[].isOwned").description("사용자 보유 여부")
                        )
                    )
                )
            )
    }

    @Test
    fun `문제 카테고리 목록 조회 API 테스트`() {
        // Given
        whenever(storeProductService.getQuestionCategories())
            .thenReturn(sampleQuestionCategories)

        // When & Then
        mockMvc.perform(
            get("/api/store/product/categories")
        )
            .andExpect(status().isOk)
            .andDo(
                document(
                    "문제 카테고리 목록 조회",
                    resourceDetails = ResourceSnippetParametersBuilder()
                        .summary("문제 카테고리 목록 조회")
                        .description("과목별로 분류된 문제 카테고리 목록을 조회합니다.")
                        .tag("hub-question"),
                    snippets = arrayOf(
                        responseFields(
                            fieldWithPath("categories").description("과목별 카테고리 목록"),
                            fieldWithPath("categories[].subject").description("과목"),
                            fieldWithPath("categories[].list").description("해당 과목의 메인 카테고리 목록"),
                            fieldWithPath("categories[].list[].title").description("메인 카테고리 제목"),
                            fieldWithPath("categories[].list[].subject").description("과목"),
                            fieldWithPath("categories[].list[].sub").description("하위 카테고리 목록"),
                            fieldWithPath("categories[].list[].sub[].id").description("하위 카테고리 ID"),
                            fieldWithPath("categories[].list[].sub[].title").description("하위 카테고리 제목")
                        )
                    )
                )
            )
    }

    @Test
    fun `문제 상세 조회 API 테스트`() {
        // Given
        val questionId = 1L

        whenever(storeProductService.getQuestionInformation(any(), any()))
            .thenReturn(sampleStoreProductDetail)

        // When & Then
        mockMvc.perform(
            get("/api/store/product/{questionId}", questionId)
                .header("Authorization", "Bearer mock_access_token")
        )
            .andExpect(status().isOk)
            .andDo(
                document(
                    "문제 상세 조회",
                    resourceDetails = ResourceSnippetParametersBuilder()
                        .summary("문제 상세 조회")
                        .description("특정 문제의 상세 정보를 조회합니다.")
                        .tag("hub-question"),
                    snippets = arrayOf(
                        pathParameters(
                            parameterWithName("questionId").description("조회할 문제 ID")
                        ),
                        responseFields(
                            fieldWithPath("productDetail").description("문제 상세 정보"),
                            fieldWithPath("productDetail.questionContent.id").description("문제 ID"),
                            fieldWithPath("productDetail.questionContent.creatorId").description("크리에이터 ID"),
                            fieldWithPath("productDetail.questionContent.title").description("문제 제목"),
                            fieldWithPath("productDetail.questionContent.subject").description("과목"),
                            fieldWithPath("productDetail.questionContent.parentCategory").description("상위 카테고리"),
                            fieldWithPath("productDetail.questionContent.childCategory").description("하위 카테고리"),
                            fieldWithPath("productDetail.questionContent.thumbnail").description("썸네일 이미지 URL"),
                            fieldWithPath("productDetail.questionContent.questionLevel").description("문제 난이도"),
                            fieldWithPath("productDetail.questionContent.price").description("문제 가격"),
                            fieldWithPath("productDetail.questionContent.rate").description("평균 평점"),
                            fieldWithPath("productDetail.creator").description("크리에이터 이름"),
                            fieldWithPath("productDetail.isOwned").description("사용자 보유 여부")
                        )
                    )
                )
            )
    }

    @Test
    fun `문제 상세 조회 API 실패 테스트 - 존재하지 않는 문제`() {
        // Given
        val nonExistentQuestionId = 999L

        whenever(storeProductService.getQuestionInformation(any(), any()))
            .thenThrow(CoreException(Error.NOT_FOUND))

        // When & Then
        mockMvc.perform(
            get("/api/store/product/{questionId}", nonExistentQuestionId)
                .header("Authorization", "Bearer mock_access_token")
        )
            .andExpect(status().isNotFound)
            .andDo(
                document(
                    "문제 상세 조회 실패 - 존재하지 않는 문제",
                    resourceDetails = ResourceSnippetParametersBuilder()
                        .summary("문제 상세 조회")
                        .description("특정 문제의 상세 정보를 조회합니다.")
                        .tag("hub-question"),
                    snippets = arrayOf(
                        pathParameters(
                            parameterWithName("questionId").description("조회할 문제 ID")
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
