package com.eager.questioncloud.application.api.user.point.controller

import com.eager.questioncloud.application.api.user.point.service.UserPointService
import com.eager.questioncloud.core.domain.point.model.UserPoint
import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document
import com.epages.restdocs.apispec.ResourceSnippetParametersBuilder
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@AutoConfigureRestDocs
class UserPointControllerDocument {

    @Autowired
    private lateinit var mockMvc: MockMvc
    
    @MockBean
    private lateinit var userPointService: UserPointService

    @Test
    fun `보유중인 포인트 조회 API 테스트`() {
        // Given
        val userPoint = UserPoint(
            userId = 1L,
            point = 12500
        )

        whenever(userPointService.getUserPoint(any()))
            .thenReturn(userPoint)

        // When & Then
        mockMvc.perform(
            get("/api/user/point")
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
