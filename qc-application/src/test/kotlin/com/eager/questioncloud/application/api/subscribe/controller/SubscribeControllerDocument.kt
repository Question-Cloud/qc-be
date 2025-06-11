package com.eager.questioncloud.application.api.subscribe.controller

import com.eager.questioncloud.application.api.subscribe.service.SubscribeService
import com.eager.questioncloud.core.exception.CoreException
import com.eager.questioncloud.core.exception.Error
import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document
import com.epages.restdocs.apispec.ResourceSnippetParametersBuilder
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*
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
class SubscribeControllerDocument {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var subscribeService: SubscribeService

    @Test
    fun `특정 크리에이터 구독 여부 확인 API 테스트`() {
        // Given
        val creatorId = 101L
        val isSubscribed = true
        val subscriberCount = 1250

        whenever(subscribeService.isSubscribed(any(), any()))
            .thenReturn(isSubscribed)
        whenever(subscribeService.countSubscriber(any()))
            .thenReturn(subscriberCount)

        // When & Then
        mockMvc.perform(
            get("/api/subscribe/status/{creatorId}", creatorId)
                .header("Authorization", "Bearer mock_access_token")
        )
            .andExpect(status().isOk)
            .andDo(
                document(
                    "특정 크리에이터 구독 여부 확인",
                    resourceDetails = ResourceSnippetParametersBuilder()
                        .summary("특정 크리에이터 구독 여부 확인")
                        .description("특정 크리에이터를 구독했는지 여부를 반환합니다.")
                        .tag("subscribe"),
                    snippets = arrayOf(
                        pathParameters(
                            parameterWithName("creatorId").description("구독 여부를 확인할 크리에이터 ID")
                        ),
                        responseFields(
                            fieldWithPath("isSubscribed").description("구독 여부"),
                            fieldWithPath("count").description("크리에이터의 총 구독자 수")
                        )
                    )
                )
            )
    }

    @Test
    fun `크리에이터 구독 API 테스트`() {
        // Given
        val creatorId = 101L

        // When & Then
        mockMvc.perform(
            post("/api/subscribe/{creatorId}", creatorId)
                .header("Authorization", "Bearer mock_access_token")
        )
            .andExpect(status().isOk)
            .andDo(
                document(
                    "크리에이터 구독",
                    resourceDetails = ResourceSnippetParametersBuilder()
                        .summary("크리에이터 구독")
                        .description("크리에이터를 구독합니다.")
                        .tag("subscribe"),
                    snippets = arrayOf(
                        pathParameters(
                            parameterWithName("creatorId").description("구독할 크리에이터 ID")
                        ),
                        responseFields(
                            fieldWithPath("success").description("요청 성공 여부")
                        )
                    )
                )
            )
    }

    @Test
    fun `크리에이터 구독 API 실패 테스트 - 존재하지 않는 크리에이터`() {
        // Given
        val creatorId = 999L

        whenever(subscribeService.subscribe(any(), any()))
            .thenThrow(CoreException(Error.NOT_FOUND))

        // When & Then
        mockMvc.perform(
            post("/api/subscribe/{creatorId}", creatorId)
                .header("Authorization", "Bearer mock_access_token")
        )
            .andExpect(status().isNotFound)
            .andDo(
                document(
                    "크리에이터 구독 실패 - 존재하지 않는 크리에이터",
                    resourceDetails = ResourceSnippetParametersBuilder()
                        .summary("크리에이터 구독")
                        .description("크리에이터를 구독합니다.")
                        .tag("subscribe"),
                    snippets = arrayOf(
                        pathParameters(
                            parameterWithName("creatorId").description("구독할 크리에이터 ID")
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
    fun `크리에이터 구독 API 실패 테스트 - 이미 구독함`() {
        // Given
        val creatorId = 101L

        whenever(subscribeService.subscribe(any(), any()))
            .thenThrow(CoreException(Error.ALREADY_SUBSCRIBE_CREATOR))

        // When & Then
        mockMvc.perform(
            post("/api/subscribe/{creatorId}", creatorId)
                .header("Authorization", "Bearer mock_access_token")
        )
            .andExpect(status().isConflict)
            .andDo(
                document(
                    "크리에이터 구독 실패 - 이미 구독함",
                    resourceDetails = ResourceSnippetParametersBuilder()
                        .summary("크리에이터 구독")
                        .description("크리에이터를 구독합니다.")
                        .tag("subscribe"),
                    snippets = arrayOf(
                        pathParameters(
                            parameterWithName("creatorId").description("구독할 크리에이터 ID")
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
    fun `크리에이터 구독 취소 API 테스트`() {
        // Given
        val creatorId = 101L

        // When & Then
        mockMvc.perform(
            delete("/api/subscribe/{creatorId}", creatorId)
                .header("Authorization", "Bearer mock_access_token")
        )
            .andExpect(status().isOk)
            .andDo(
                document(
                    "크리에이터 구독 취소",
                    resourceDetails = ResourceSnippetParametersBuilder()
                        .summary("크리에이터 구독 취소")
                        .description("크리에이터 구독을 취소합니다.")
                        .tag("subscribe"),
                    snippets = arrayOf(
                        pathParameters(
                            parameterWithName("creatorId").description("구독 취소할 크리에이터 ID")
                        ),
                        responseFields(
                            fieldWithPath("success").description("요청 성공 여부")
                        )
                    )
                )
            )
    }
}
