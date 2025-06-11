package com.eager.questioncloud.application.api.user.account.controller

import com.eager.questioncloud.application.api.user.account.dto.ChangePasswordRequest
import com.eager.questioncloud.application.api.user.account.service.UserAccountService
import com.eager.questioncloud.core.exception.CoreException
import com.eager.questioncloud.core.exception.Error
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
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.request.RequestDocumentation.queryParameters
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@AutoConfigureRestDocs
class UserAccountControllerDocument {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @MockBean
    private lateinit var userAccountService: UserAccountService

    @Test
    fun `계정 찾기 API 테스트`() {
        // Given
        val phone = "01012345678"
        val email = "user@example.com"

        whenever(userAccountService.recoverForgottenEmail(any()))
            .thenReturn(email)

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

    @Test
    fun `계정 찾기 API 실패 테스트 - 계정 없음`() {
        // Given
        val phone = "01099999999"

        whenever(userAccountService.recoverForgottenEmail(any()))
            .thenThrow(CoreException(Error.NOT_FOUND))

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

    @Test
    fun `비밀번호 찾기 API 테스트`() {
        // Given
        val email = "user@example.com"

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

    @Test
    fun `비밀번호 찾기 API 실패 테스트 - 계정 없음`() {
        // Given
        val email = "notfound@example.com"

        whenever(userAccountService.sendRecoverForgottenPasswordMail(any()))
            .thenThrow(CoreException(Error.FAIL_LOGIN))

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

    @Test
    fun `비밀번호 변경 메일 요청 API 테스트`() {
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

    @Test
    fun `비밀번호 변경 API 테스트`() {
        // Given
        val changePasswordRequest = ChangePasswordRequest(
            token = "mock_verification_token_12345",
            newPassword = "NewPassword123!"
        )

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

    @Test
    fun `비밀번호 변경 API 실패 테스트 - 유효하지 않은 토큰`() {
        // Given
        val changePasswordRequest = ChangePasswordRequest(
            token = "invalid_token",
            newPassword = "NewPassword123!"
        )

        whenever(userAccountService.changePassword(any(), any()))
            .thenThrow(CoreException(Error.NOT_FOUND))

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
