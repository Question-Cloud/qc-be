package com.eager.questioncloud.creator.controller

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import com.eager.questioncloud.creator.domain.CreatorProfile
import com.eager.questioncloud.creator.dto.CreatorInformation
import com.eager.questioncloud.creator.service.CreatorInformationService
import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document
import com.epages.restdocs.apispec.ResourceSnippetParametersBuilder
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
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
import org.springframework.restdocs.request.RequestDocumentation.pathParameters
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@AutoConfigureRestDocs
class CreatorControllerDocument {
    @Autowired
    private lateinit var mockMvc: MockMvc
    
    @MockBean
    private lateinit var creatorInformationService: CreatorInformationService
    
    private lateinit var sampleCreatorInformation: CreatorInformation
    
    @BeforeEach
    fun setUp() {
        val sampleCreatorProfile = CreatorProfile(
            creatorId = 1L,
            name = "김수학",
            profileImage = "https://example.com/profile.jpg",
            mainSubject = "Mathematics",
            email = "creator@example.com",
            introduction = "수학 전문 크리에이터입니다."
        )
        
        sampleCreatorInformation = CreatorInformation(
            creatorProfile = sampleCreatorProfile,
            salesCount = 150,
            averageRateOfReview = 4.8,
            subscriberCount = 523
        )
    }
    
    @Test
    fun `크리에이터 정보 조회 API 테스트`() {
        // Given
        val creatorId = 1L
        
        whenever(creatorInformationService.getCreatorInformation(creatorId))
            .thenReturn(sampleCreatorInformation)
        
        // When & Then
        mockMvc.perform(
            get("/api/creator/info/{creatorId}", creatorId)
        )
            .andExpect(status().isOk)
            .andDo(
                document(
                    "크리에이터 정보 조회",
                    resourceDetails = ResourceSnippetParametersBuilder()
                        .summary("크리에이터 정보 조회")
                        .description("특정 크리에이터의 상세 정보를 조회합니다.")
                        .tag("creator"),
                    snippets = arrayOf(
                        pathParameters(
                            parameterWithName("creatorId").description("조회할 크리에이터 ID")
                        ),
                        responseFields(
                            fieldWithPath("creatorInformation").description("크리에이터 정보"),
                            fieldWithPath("creatorInformation.creatorProfile").description("크리에이터 프로필 정보"),
                            fieldWithPath("creatorInformation.creatorProfile.creatorId").description("크리에이터 ID"),
                            fieldWithPath("creatorInformation.creatorProfile.name").description("크리에이터 이름"),
                            fieldWithPath("creatorInformation.creatorProfile.profileImage").description("프로필 이미지 URL")
                                .optional(),
                            fieldWithPath("creatorInformation.creatorProfile.mainSubject").description("주요 과목"),
                            fieldWithPath("creatorInformation.creatorProfile.email").description("이메일 주소"),
                            fieldWithPath("creatorInformation.creatorProfile.introduction").description("자기소개"),
                            fieldWithPath("creatorInformation.salesCount").description("총 판매 건수"),
                            fieldWithPath("creatorInformation.averageRateOfReview").description("평균 리뷰 평점"),
                            fieldWithPath("creatorInformation.subscriberCount").description("구독자 수")
                        )
                    )
                )
            )
    }
    
    @Test
    fun `크리에이터 정보 조회 API 실패 테스트 - 존재하지 않는 크리에이터`() {
        // Given
        val nonExistentCreatorId = 999L
        
        whenever(creatorInformationService.getCreatorInformation(nonExistentCreatorId))
            .thenThrow(CoreException(Error.NOT_FOUND))
        
        // When & Then
        mockMvc.perform(
            get("/api/creator/info/{creatorId}", nonExistentCreatorId)
        )
            .andExpect(status().isNotFound)
            .andDo(
                document(
                    "크리에이터 정보 조회 실패 - 존재하지 않는 크리에이터",
                    resourceDetails = ResourceSnippetParametersBuilder()
                        .summary("크리에이터 정보 조회")
                        .description("특정 크리에이터의 상세 정보를 조회합니다.")
                        .tag("creator"),
                    snippets = arrayOf(
                        pathParameters(
                            parameterWithName("creatorId").description("조회할 크리에이터 ID")
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
