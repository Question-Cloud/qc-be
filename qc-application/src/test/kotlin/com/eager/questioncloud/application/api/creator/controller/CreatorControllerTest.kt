package com.eager.questioncloud.application.api.creator.controller

import com.eager.questioncloud.application.api.creator.dto.CreatorInformation
import com.eager.questioncloud.application.api.creator.dto.CreatorInformationResponse
import com.eager.questioncloud.application.api.creator.service.CreatorInformationService
import com.eager.questioncloud.core.domain.creator.dto.CreatorProfile
import com.eager.questioncloud.core.domain.question.enums.Subject
import com.eager.questioncloud.core.exception.CoreException
import com.eager.questioncloud.core.exception.Error
import com.epages.restdocs.apispec.ResourceSnippetParametersBuilder
import com.epages.restdocs.apispec.RestAssuredRestDocumentationWrapper.document
import io.restassured.RestAssured
import io.restassured.builder.RequestSpecBuilder
import io.restassured.http.ContentType
import io.restassured.specification.RequestSpecification
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.kotlin.whenever
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.restassured.RestAssuredRestDocumentation.documentationConfiguration
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class, RestDocumentationExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureRestDocs(outputDir = "build/generated-snippets")
@ActiveProfiles("test")
class CreatorControllerTest {
    @LocalServerPort
    private var port: Int = 0

    @MockBean
    private lateinit var creatorInformationService: CreatorInformationService

    private lateinit var spec: RequestSpecification

    private lateinit var sampleCreatorInformation: CreatorInformation

    @BeforeEach
    fun setUp(restDocumentation: RestDocumentationContextProvider) {
        val sampleCreatorProfile = CreatorProfile(
            creatorId = 1L,
            name = "김수학",
            profileImage = "https://example.com/profile.jpg",
            mainSubject = Subject.Mathematics,
            email = "creator@example.com",
            introduction = "수학 전문 크리에이터입니다."
        )

        sampleCreatorInformation = CreatorInformation(
            creatorProfile = sampleCreatorProfile,
            salesCount = 150,
            averageRateOfReview = 4.8,
            subscriberCount = 523
        )

        spec = RequestSpecBuilder()
            .addFilter(documentationConfiguration(restDocumentation))
            .setContentType(ContentType.JSON)
            .setPort(port)
            .build()
    }

    @Test
    fun `크리에이터 정보 조회 API 테스트`() {
        // Given
        val creatorId = 1L

        whenever(creatorInformationService.getCreatorInformation(creatorId))
            .thenReturn(sampleCreatorInformation)

        // When & Then
        RestAssured.given(spec)
            .filter(
                document(
                    "크리에이터 정보 조회",
                    ResourceSnippetParametersBuilder()
                        .summary("크리에이터 정보 조회")
                        .description("특정 크리에이터의 상세 정보를 조회합니다.")
                        .tag("creator")
                        .pathParameters(
                            parameterWithName("creatorId").description("조회할 크리에이터 ID")
                        )
                        .responseFields(
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
            .`when`()
            .get("/api/creator/info/{creatorId}", creatorId)
            .then()
            .statusCode(200)
            .extract().response().`as`(CreatorInformationResponse::class.java)
    }

    @Test
    fun `크리에이터 정보 조회 API 실패 테스트 - 존재하지 않는 크리에이터`() {
        // Given
        val nonExistentCreatorId = 999L

        whenever(creatorInformationService.getCreatorInformation(nonExistentCreatorId))
            .thenThrow(CoreException(Error.NOT_FOUND))

        // When & Then
        RestAssured.given(spec)
            .filter(
                document(
                    "크리에이터 정보 조회 실패 - 존재하지 않는 크리에이터",
                    ResourceSnippetParametersBuilder()
                        .summary("크리에이터 정보 조회")
                        .description("특정 크리에이터의 상세 정보를 조회합니다.")
                        .tag("creator")
                        .pathParameters(
                            parameterWithName("creatorId").description("존재하지 않는 크리에이터 ID")
                        )
                        .responseFields(
                            fieldWithPath("status").description("HTTP 상태 코드"),
                            fieldWithPath("message").description("에러 메시지")
                        )
                )
            )
            .`when`()
            .get("/api/creator/info/{creatorId}", nonExistentCreatorId)
            .then()
            .statusCode(404)
    }
}
