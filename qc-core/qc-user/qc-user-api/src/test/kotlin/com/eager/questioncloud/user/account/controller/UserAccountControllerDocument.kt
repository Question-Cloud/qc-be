package com.eager.questioncloud.user.account.controller

import com.eager.questioncloud.application.security.JwtAuthenticationFilter
import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import com.eager.questioncloud.user.account.dto.ChangePasswordRequest
import com.eager.questioncloud.user.account.service.UserAccountService
import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document
import com.epages.restdocs.apispec.ResourceSnippetParametersBuilder
import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.kotest.core.extensions.ApplyExtension
import io.kotest.core.spec.style.FunSpec
import io.kotest.extensions.spring.SpringExtension
import io.mockk.every
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.FilterType
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.request.RequestDocumentation.queryParameters
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(
    controllers = [UserAccountController::class],
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
class UserAccountControllerDocument(
    private val mockMvc: MockMvc,
    private val objectMapper: ObjectMapper
) : FunSpec() {
    @MockkBean
    private lateinit var userAccountService: UserAccountService
    
    init {
        test("계정 찾기 API 테스트") {
            // Given
            val phone = "01012345678"
            val email = "user@example.com"
            
            every { userAccountService.recoverForgottenEmail(any()) } returns email
            
            // When & Then
            mockMvc.perform(
                get("/api/user/account/recover/email")
                    .param("phone", phone)
            )
                .andExpect(status().isOk)
                .andDo(
                    document(
                        "계정 찾기",
                        resourceDetails = ResourceSnippetParametersBuilder()
                            .summary("계정 찾기")
                            .description("전화번호를 통해 계정(이메일)을 찾습니다.")
                            .tag("user-account"),
                        snippets = arrayOf(
                            queryParameters(
                                parameterWithName("phone").description("계정을 찾을 전화번호")
                            ),
                            responseFields(
                                fieldWithPath("email").description("찾은 계정 이메일")
                            )
                        )
                    )
                )
        }
        
        test("계정 찾기 API 실패 테스트 - 계정 없음") {
            // Given
            val phone = "01099999999"
            
            every { userAccountService.recoverForgottenEmail(any()) } throws CoreException(Error.NOT_FOUND)
            
            // When & Then
            mockMvc.perform(
                get("/api/user/account/recover/email")
                    .param("phone", phone)
            )
                .andExpect(status().isNotFound)
                .andDo(
                    document(
                        "계정 찾기 실패 - 계정 없음",
                        resourceDetails = ResourceSnippetParametersBuilder()
                            .summary("계정 찾기")
                            .description("전화번호를 통해 계정(이메일)을 찾습니다.")
                            .tag("user-account"),
                        snippets = arrayOf(
                            queryParameters(
                                parameterWithName("phone").description("계정을 찾을 전화번호")
                            ),
                            responseFields(
                                fieldWithPath("status").description("HTTP 상태 코드"),
                                fieldWithPath("message").description("에러 메시지")
                            )
                        )
                    )
                )
        }
        
        test("비밀번호 찾기 API 테스트") {
            // Given
            val email = "user@example.com"
            
            every { userAccountService.sendRecoverForgottenPasswordMail(any()) } returns Unit
            
            // When & Then
            mockMvc.perform(
                get("/api/user/account/recover/password")
                    .param("email", email)
            )
                .andExpect(status().isOk)
                .andDo(
                    document(
                        "비밀번호 찾기",
                        resourceDetails = ResourceSnippetParametersBuilder()
                            .summary("비밀번호 찾기")
                            .description("이메일로 비밀번호 재설정 메일을 발송합니다.")
                            .tag("user-account"),
                        snippets = arrayOf(
                            queryParameters(
                                parameterWithName("email").description("비밀번호 재설정 메일을 받을 이메일")
                            ),
                            responseFields(
                                fieldWithPath("success").description("요청 성공 여부")
                            )
                        )
                    )
                )
        }
        
        test("비밀번호 찾기 API 실패 테스트 - 계정 없음") {
            // Given
            val email = "notfound@example.com"
            
            every { userAccountService.sendRecoverForgottenPasswordMail(any()) } throws CoreException(Error.FAIL_LOGIN)
            
            // When & Then
            mockMvc.perform(
                get("/api/user/account/recover/password")
                    .param("email", email)
            )
                .andExpect(status().isUnauthorized)
                .andDo(
                    document(
                        "비밀번호 찾기 실패 - 계정 없음",
                        resourceDetails = ResourceSnippetParametersBuilder()
                            .summary("비밀번호 찾기")
                            .description("이메일로 비밀번호 재설정 메일을 발송합니다.")
                            .tag("user-account"),
                        snippets = arrayOf(
                            queryParameters(
                                parameterWithName("email").description("비밀번호 재설정 메일을 받을 이메일")
                            ),
                            responseFields(
                                fieldWithPath("status").description("HTTP 상태 코드"),
                                fieldWithPath("message").description("에러 메시지")
                            )
                        )
                    )
                )
        }
        
        test("비밀번호 변경 메일 요청 API 테스트") {
            // Given
            every { userAccountService.sendChangePasswordMail(any()) } returns Unit
            
            // When & Then
            mockMvc.perform(
                get("/api/user/account/change-password-mail")
                    .header("Authorization", "Bearer mock_access_token")
            )
                .andExpect(status().isOk)
                .andDo(
                    document(
                        "비밀번호 변경 메일 요청",
                        resourceDetails = ResourceSnippetParametersBuilder()
                            .summary("비밀번호 변경 메일 요청")
                            .description("로그인한 사용자에게 비밀번호 변경 메일을 발송합니다.")
                            .tag("user-account"),
                        snippets = arrayOf(
                            responseFields(
                                fieldWithPath("success").description("요청 성공 여부")
                            )
                        )
                    )
                )
        }
        
        test("비밀번호 변경 API 테스트") {
            // Given
            val changePasswordRequest = ChangePasswordRequest(
                token = "mock_verification_token_12345",
                newPassword = "NewPassword123!"
            )
            
            every { userAccountService.changePassword(any(), any()) } returns Unit
            
            // When & Then
            mockMvc.perform(
                post("/api/user/account/change-password")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(changePasswordRequest))
            )
                .andExpect(status().isOk)
                .andDo(
                    document(
                        "비밀번호 변경",
                        resourceDetails = ResourceSnippetParametersBuilder()
                            .summary("비밀번호 변경")
                            .description("이메일 인증 토큰을 통해 비밀번호를 변경합니다.")
                            .tag("user-account"),
                        snippets = arrayOf(
                            requestFields(
                                fieldWithPath("token").description("이메일로 받은 인증 토큰"),
                                fieldWithPath("newPassword").description("새로운 비밀번호 (8자 이상, 영문+숫자+특수문자 조합)")
                            ),
                            responseFields(
                                fieldWithPath("success").description("요청 성공 여부")
                            )
                        )
                    )
                )
        }
        
        test("비밀번호 변경 API 실패 테스트 - 유효하지 않은 토큰") {
            // Given
            val changePasswordRequest = ChangePasswordRequest(
                token = "invalid_token",
                newPassword = "NewPassword123!"
            )
            
            every { userAccountService.changePassword(any(), any()) } throws CoreException(Error.NOT_FOUND)
            
            // When & Then
            mockMvc.perform(
                post("/api/user/account/change-password")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(changePasswordRequest))
            )
                .andExpect(status().isNotFound)
                .andDo(
                    document(
                        "비밀번호 변경 실패 - 유효하지 않은 토큰",
                        resourceDetails = ResourceSnippetParametersBuilder()
                            .summary("비밀번호 변경")
                            .description("이메일 인증 토큰을 통해 비밀번호를 변경합니다.")
                            .tag("user-account"),
                        snippets = arrayOf(
                            requestFields(
                                fieldWithPath("token").description("이메일로 받은 인증 토큰"),
                                fieldWithPath("newPassword").description("새로운 비밀번호 (8자 이상, 영문+숫자+특수문자 조합)")
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
}
