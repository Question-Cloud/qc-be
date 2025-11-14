package com.eager.questioncloud.subscribe.controller

import com.eager.questioncloud.application.security.JwtAuthenticationFilter
import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import com.eager.questioncloud.subscribe.dto.UserSubscriptionDetail
import com.eager.questioncloud.subscribe.service.SubscribeService
import com.eager.questioncloud.subscribe.service.UserSubscriptionService
import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document
import com.epages.restdocs.apispec.ResourceSnippetParametersBuilder
import com.ninjasquad.springmockk.MockkBean
import io.kotest.core.extensions.ApplyExtension
import io.kotest.core.spec.style.FunSpec
import io.kotest.extensions.spring.SpringExtension
import io.mockk.every
import io.mockk.justRun
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.FilterType
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.restdocs.request.RequestDocumentation.*
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(
    controllers = [SubscribeController::class],
    excludeFilters = [
        ComponentScan.Filter(
            type = FilterType.ASSIGNABLE_TYPE,
            classes = [JwtAuthenticationFilter::class]
        ),
    ]
)
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureRestDocs
@ApplyExtension(SpringExtension::class)
class SubscribeControllerDocument(
    private val mockMvc: MockMvc,
) : FunSpec() {
    @MockkBean
    private lateinit var subscribeService: SubscribeService
    
    @MockkBean
    private lateinit var userSubscriptionService: UserSubscriptionService
    
    private val sampleCreatorInformations = listOf(
        UserSubscriptionDetail(
            creatorId = 1L,
            creatorName = "크리에이터 1",
            profileImage = "https://example.com/profile1.jpg",
            subscriberCount = 1000,
            mainSubject = "Mathematics"
        ),
        UserSubscriptionDetail(
            creatorId = 2L,
            creatorName = "크리에이터 2",
            profileImage = "https://example.com/profile2.jpg",
            subscriberCount = 1234,
            mainSubject = "Physics"
        )
    )
    
    init {
        test("특정 크리에이터 구독 여부 확인 API 테스트") {
            val creatorId = 101L
            val isSubscribed = true
            
            every { userSubscriptionService.isSubscribed(any(), any()) } returns isSubscribed
            
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
                                fieldWithPath("isSubscribed").description("구독 여부")
                            )
                        )
                    )
                )
        }
        
        test("크리에이터 구독 API 테스트") {
            val creatorId = 101L
            
            justRun { subscribeService.subscribe(any(), any()) }
            
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
        
        test("나의 구독 목록 조회 API 테스트") {
            val totalCount = 5
            
            every { userSubscriptionService.countMySubscribe(any()) } returns totalCount
            every { userSubscriptionService.getMySubscribes(any(), any()) } returns sampleCreatorInformations
            
            mockMvc.perform(
                get("/api/subscribe/my-subscribe")
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
                                fieldWithPath("result[].creatorId").description("크리에이터 ID"),
                                fieldWithPath("result[].creatorName").description("크리에이터 이름"),
                                fieldWithPath("result[].profileImage").description("프로필 이미지 URL").optional(),
                                fieldWithPath("result[].mainSubject").description("주요 과목"),
                                fieldWithPath("result[].subscriberCount").description("구독자 수")
                            )
                        )
                    )
                )
        }
        
        test("크리에이터 구독 API 실패 테스트 - 존재하지 않는 크리에이터") {
            val creatorId = 999L
            
            every { subscribeService.subscribe(any(), any()) } throws CoreException(Error.NOT_FOUND)
            
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
        
        test("크리에이터 구독 API 실패 테스트 - 이미 구독함") {
            val creatorId = 101L
            
            every { subscribeService.subscribe(any(), any()) } throws CoreException(Error.ALREADY_SUBSCRIBE_CREATOR)
            
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
        
        test("크리에이터 구독 취소 API 테스트") {
            val creatorId = 101L
            
            justRun { subscribeService.unSubscribe(any(), any()) }
            
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
}