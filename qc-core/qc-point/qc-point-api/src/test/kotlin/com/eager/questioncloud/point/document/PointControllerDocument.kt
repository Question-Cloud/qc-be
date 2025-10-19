package com.eager.questioncloud.point.document

import com.eager.questioncloud.application.security.JwtAuthenticationFilter
import com.eager.questioncloud.filter.FilterExceptionHandlerFilter
import com.eager.questioncloud.point.controller.PointController
import com.eager.questioncloud.point.domain.UserPoint
import com.eager.questioncloud.point.service.UserPointService
import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document
import com.epages.restdocs.apispec.ResourceSnippetParametersBuilder
import com.ninjasquad.springmockk.MockkBean
import io.kotest.core.extensions.ApplyExtension
import io.kotest.core.spec.style.FunSpec
import io.kotest.extensions.spring.SpringExtension
import io.mockk.every
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.FilterType
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(
    controllers = [PointController::class],
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
class PointControllerDocument : FunSpec() {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockkBean
    private lateinit var userPointService: UserPointService

    init {
        test("보유중인 포인트 조회 API 테스트") {
            // Given
            val userPoint = UserPoint(
                userId = 1L,
                point = 12500
            )

            every { userPointService.getUserPoint(any()) } returns userPoint

            // When & Then
            mockMvc.perform(
                get("/api/payment/point")
                    .header("Authorization", "Bearer mock_access_token")
            )
                .andExpect(status().isOk)
                .andDo(
                    document(
                        "보유중인 포인트 조회",
                        resourceDetails = ResourceSnippetParametersBuilder()
                            .summary("보유중인 포인트 조회")
                            .description("사용자가 보유한 포인트를 조회합니다.")
                            .tag("point"),
                        snippets = arrayOf(
                            responseFields(
                                fieldWithPath("point").description("보유 포인트")
                            )
                        )
                    )
                )
        }
    }
}