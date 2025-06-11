package com.eager.questioncloud.application.api.feed.subscribe.controller

import com.eager.questioncloud.application.api.creator.dto.CreatorInformation
import com.eager.questioncloud.application.api.feed.subscribe.service.FeedSubscribeService
import com.eager.questioncloud.core.domain.creator.dto.CreatorProfile
import com.eager.questioncloud.core.domain.question.enums.Subject
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
class FeedSubscribeControllerDocument {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var feedSubscribeService: FeedSubscribeService

    private lateinit var sampleCreatorInformations: List<CreatorInformation>

    @BeforeEach
    fun setUp() {
        val creatorProfile1 = CreatorProfile(
            creatorId = 1L,
            name = "김수학",
            profileImage = "https://example.com/profile1.jpg",
            mainSubject = Subject.Mathematics,
            email = "math@example.com",
            introduction = "수학 전문 크리에이터입니다."
        )

        val creatorProfile2 = CreatorProfile(
            creatorId = 2L,
            name = "이물리",
            profileImage = "https://example.com/profile2.jpg",
            mainSubject = Subject.Physics,
            email = "physics@example.com",
            introduction = "물리 전문 크리에이터입니다."
        )

        sampleCreatorInformations = listOf(
            CreatorInformation(
                creatorProfile = creatorProfile1,
                salesCount = 150,
                averageRateOfReview = 4.8,
                subscriberCount = 523
            ),
            CreatorInformation(
                creatorProfile = creatorProfile2,
                salesCount = 89,
                averageRateOfReview = 4.5,
                subscriberCount = 312
            )
        )
    }

    @Test
    fun `나의 구독 목록 조회 API 테스트`() {
        // Given
        val totalCount = 5

        whenever(feedSubscribeService.countMySubscribe(any()))
            .thenReturn(totalCount)
        whenever(feedSubscribeService.getMySubscribes(any(), any()))
            .thenReturn(sampleCreatorInformations)

        // When & Then
        mockMvc.perform(
            get("/api/feed/subscribe/my-subscribe")
                .header("Authorization", "Bearer mock_access_token")
                .param("page", "1")
                .param("size", "10")
        )
            .andExpect(status().isOk)
            .andDo(
                document(
                    "나의 구독 목록 조회",
                    resourceDetails = ResourceSnippetParametersBuilder()
                        .summary("나의 구독 목록 조회")
                        .description("사용자가 구독한 크리에이터 목록을 페이징하여 조회합니다.")
                        .tag("subscribe"),
                    snippets = arrayOf(
                        queryParameters(
                            parameterWithName("page").description("페이지 번호 (1부터 시작)"),
                            parameterWithName("size").description("페이지당 항목 수")
                        ),
                        responseFields(
                            fieldWithPath("total").description("전체 구독 크리에이터 수"),
                            fieldWithPath("result").description("구독 크리에이터 목록"),
                            fieldWithPath("result[].creatorProfile").description("크리에이터 프로필 정보"),
                            fieldWithPath("result[].creatorProfile.creatorId").description("크리에이터 ID"),
                            fieldWithPath("result[].creatorProfile.name").description("크리에이터 이름"),
                            fieldWithPath("result[].creatorProfile.profileImage").description("프로필 이미지 URL").optional(),
                            fieldWithPath("result[].creatorProfile.mainSubject").description("주요 과목"),
                            fieldWithPath("result[].creatorProfile.email").description("이메일 주소"),
                            fieldWithPath("result[].creatorProfile.introduction").description("자기소개"),
                            fieldWithPath("result[].salesCount").description("총 판매 건수"),
                            fieldWithPath("result[].averageRateOfReview").description("평균 리뷰 평점"),
                            fieldWithPath("result[].subscriberCount").description("구독자 수")
                        )
                    )
                )
            )
    }
}
