package com.eager.questioncloud.user.register.controller

import com.eager.questioncloud.application.security.JwtAuthenticationFilter
import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import com.eager.questioncloud.filter.FilterExceptionHandlerFilter
import com.eager.questioncloud.user.domain.EmailVerification
import com.eager.questioncloud.user.domain.User
import com.eager.questioncloud.user.domain.UserAccountInformation
import com.eager.questioncloud.user.domain.UserInformation
import com.eager.questioncloud.user.enums.AccountType
import com.eager.questioncloud.user.enums.EmailVerificationType
import com.eager.questioncloud.user.enums.UserStatus
import com.eager.questioncloud.user.enums.UserType
import com.eager.questioncloud.user.register.dto.CreateUserRequest
import com.eager.questioncloud.user.register.service.RegisterUserService
import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document
import com.epages.restdocs.apispec.ResourceSnippetParametersBuilder
import com.fasterxml.jackson.databind.ObjectMapper
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
    controllers = [RegisterUserController::class],
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
class RegisterUserControllerDocument(
    private val mockMvc: MockMvc,
    private val objectMapper: ObjectMapper
) : FunSpec() {
    @MockkBean
    lateinit var registerUserService: RegisterUserService
    
    private lateinit var sampleUser: User
    private lateinit var sampleEmailVerification: EmailVerification
    
    init {
        beforeTest {
            val userAccountInformation = UserAccountInformation.createEmailAccountInformation("hashedPassword123")
            val userInformation = UserInformation(
                profileImage = "default",
                name = "김회원",
                email = "newuser@example.com",
                phone = "01012345678"
            )
            
            sampleUser = User.create(
                userAccountInformation = userAccountInformation,
                userInformation = userInformation,
                userType = UserType.NormalUser,
                userStatus = UserStatus.PendingEmailVerification
            )
            sampleUser.uid = 123L
            
            sampleEmailVerification = EmailVerification(
                uid = 123L,
                email = "newuser@example.com",
                token = "verification_token_12345",
                resendToken = "resend_token_67890",
                emailVerificationType = EmailVerificationType.CreateUser,
            )
        }
        
        test("회원가입 API 테스트 - 이메일 회원가입") {
            // Given
            val createUserRequest = CreateUserRequest(
                email = "newuser@example.com",
                password = "Password123!",
                socialRegisterToken = null,
                accountType = AccountType.EMAIL,
                phone = "01012345678",
                name = "김회원"
            )
            
            every { registerUserService.register(any()) } returns sampleEmailVerification
            
            // When & Then
            mockMvc.perform(
                post("/api/user/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(createUserRequest))
            )
                .andExpect(status().isOk)
                .andDo(
                    document(
                        "회원가입 - 이메일",
                        resourceDetails = ResourceSnippetParametersBuilder()
                            .summary("회원가입")
                            .description("새로운 사용자를 등록합니다.")
                            .tag("user-register"),
                        snippets = arrayOf(
                            requestFields(
                                fieldWithPath("email").description("사용자 이메일"),
                                fieldWithPath("password").description("비밀번호 (이메일 회원가입 시 필수, 8자 이상 영문+숫자+특수문자 조합)"),
                                fieldWithPath("socialRegisterToken").description("소셜 회원가입 토큰 (소셜 회원가입 시 필수, 이메일 회원가입 시 null)"),
                                fieldWithPath("accountType").description("계정 타입 (EMAIL, KAKAO, GOOGLE, NAVER, GUEST)"),
                                fieldWithPath("phone").description("전화번호"),
                                fieldWithPath("name").description("사용자 이름 (2자 이상)")
                            ),
                            responseFields(
                                fieldWithPath("resendToken").description("인증 메일 재발송용 토큰")
                            )
                        )
                    )
                )
        }
        
        test("회원가입 API 테스트 - 소셜 회원가입") {
            // Given
            val createUserRequest = CreateUserRequest(
                email = "socialuser@example.com",
                password = null,
                socialRegisterToken = "social_register_token_12345",
                accountType = AccountType.KAKAO,
                phone = "01087654321",
                name = "이소셜"
            )
            
            every { registerUserService.register(any()) } returns sampleEmailVerification
            
            // When & Then
            mockMvc.perform(
                post("/api/user/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(createUserRequest))
            )
                .andExpect(status().isOk)
                .andDo(
                    document(
                        "회원가입 - 소셜",
                        resourceDetails = ResourceSnippetParametersBuilder()
                            .summary("회원가입")
                            .description("새로운 사용자를 등록합니다.")
                            .tag("user-register"),
                        snippets = arrayOf(
                            requestFields(
                                fieldWithPath("email").description("사용자 이메일"),
                                fieldWithPath("password").description("비밀번호 (소셜 회원가입 시 null)"),
                                fieldWithPath("socialRegisterToken").description("소셜 회원가입 토큰"),
                                fieldWithPath("accountType").description("계정 타입 (KAKAO)"),
                                fieldWithPath("phone").description("전화번호"),
                                fieldWithPath("name").description("사용자 이름 (2자 이상)")
                            ),
                            responseFields(
                                fieldWithPath("resendToken").description("인증 메일 재발송용 토큰")
                            )
                        )
                    )
                )
        }
        
        test("회원가입 API 실패 테스트 - 중복 이메일") {
            // Given
            val createUserRequest = CreateUserRequest(
                email = "duplicate@example.com",
                password = "Password123!",
                socialRegisterToken = null,
                accountType = AccountType.EMAIL,
                phone = "01012345678",
                name = "김회원"
            )
            
            every { registerUserService.register(any()) } throws CoreException(Error.DUPLICATE_EMAIL)
            
            // When & Then
            mockMvc.perform(
                post("/api/user/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(createUserRequest))
            )
                .andExpect(status().isConflict)
                .andDo(
                    document(
                        "회원가입 실패 - 중복 이메일",
                        resourceDetails = ResourceSnippetParametersBuilder()
                            .summary("회원가입")
                            .description("새로운 사용자를 등록합니다.")
                            .tag("user-register"),
                        snippets = arrayOf(
                            requestFields(
                                fieldWithPath("email").description("사용자 이메일"),
                                fieldWithPath("password").description("비밀번호"),
                                fieldWithPath("socialRegisterToken").description("소셜 회원가입 토큰 (null)"),
                                fieldWithPath("accountType").description("계정 타입"),
                                fieldWithPath("phone").description("전화번호"),
                                fieldWithPath("name").description("사용자 이름")
                            ),
                            responseFields(
                                fieldWithPath("status").description("HTTP 상태 코드"),
                                fieldWithPath("message").description("에러 메시지")
                            )
                        )
                    )
                )
        }
        
        test("회원가입 API 실패 테스트 - 중복 전화번호") {
            // Given
            val createUserRequest = CreateUserRequest(
                email = "newuser@example.com",
                password = "Password123!",
                socialRegisterToken = null,
                accountType = AccountType.EMAIL,
                phone = "01012345678",
                name = "김회원"
            )
            
            every { registerUserService.register(any()) } throws CoreException(Error.DUPLICATE_PHONE)
            
            // When & Then
            mockMvc.perform(
                post("/api/user/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(createUserRequest))
            )
                .andExpect(status().isConflict)
                .andDo(
                    document(
                        "회원가입 실패 - 중복 전화번호",
                        resourceDetails = ResourceSnippetParametersBuilder()
                            .summary("회원가입")
                            .description("새로운 사용자를 등록합니다.")
                            .tag("user-register"),
                        snippets = arrayOf(
                            requestFields(
                                fieldWithPath("email").description("사용자 이메일"),
                                fieldWithPath("password").description("비밀번호"),
                                fieldWithPath("socialRegisterToken").description("소셜 회원가입 토큰 (null)"),
                                fieldWithPath("accountType").description("계정 타입"),
                                fieldWithPath("phone").description("전화번호"),
                                fieldWithPath("name").description("사용자 이름")
                            ),
                            responseFields(
                                fieldWithPath("status").description("HTTP 상태 코드"),
                                fieldWithPath("message").description("에러 메시지")
                            )
                        )
                    )
                )
        }
        
        test("회원가입 인증 메일 재요청 API 테스트") {
            // Given
            val resendToken = "resend_token_67890"
            
            justRun { registerUserService.resend(any()) }
            
            // When & Then
            mockMvc.perform(
                get("/api/user/register/resend-verification-mail")
                    .param("resendToken", resendToken)
            )
                .andExpect(status().isOk)
                .andDo(
                    document(
                        "회원가입 인증 메일 재요청",
                        resourceDetails = ResourceSnippetParametersBuilder()
                            .summary("회원가입 인증 메일 재요청")
                            .description("회원가입 인증 메일을 재발송합니다.")
                            .tag("user-register"),
                        snippets = arrayOf(
                            queryParameters(
                                parameterWithName("resendToken").description("인증 메일 재발송용 토큰")
                            ),
                            responseFields(
                                fieldWithPath("success").description("요청 성공 여부")
                            )
                        )
                    )
                )
        }
        
        test("회원가입 인증 메일 재요청 API 실패 테스트 - 유효하지 않은 토큰") {
            // Given
            val resendToken = "invalid_resend_token"
            
            every { registerUserService.resend(any()) } throws CoreException(Error.NOT_FOUND)
            
            // When & Then
            mockMvc.perform(
                get("/api/user/register/resend-verification-mail")
                    .param("resendToken", resendToken)
            )
                .andExpect(status().isNotFound)
                .andDo(
                    document(
                        "회원가입 인증 메일 재요청 실패 - 유효하지 않은 토큰",
                        resourceDetails = ResourceSnippetParametersBuilder()
                            .summary("회원가입 인증 메일 재요청")
                            .description("회원가입 인증 메일을 재발송합니다.")
                            .tag("user-register"),
                        snippets = arrayOf(
                            queryParameters(
                                parameterWithName("resendToken").description("인증 메일 재발송용 토큰")
                            ),
                            responseFields(
                                fieldWithPath("status").description("HTTP 상태 코드"),
                                fieldWithPath("message").description("에러 메시지")
                            )
                        )
                    )
                )
        }
        
        test("회원가입 인증 메일 확인 API 테스트") {
            // Given
            val token = "verification_token_12345"
            
            justRun { registerUserService.verifyCreateUser(any(), any()) }
            
            // When & Then
            mockMvc.perform(
                get("/api/user/register/verify")
                    .param("token", token)
            )
                .andExpect(status().isOk)
                .andDo(
                    document(
                        "회원가입 인증 메일 확인",
                        resourceDetails = ResourceSnippetParametersBuilder()
                            .summary("회원가입 인증 메일 확인")
                            .description("회원가입 인증 메일의 토큰을 확인하여 계정을 활성화합니다.")
                            .tag("user-register"),
                        snippets = arrayOf(
                            queryParameters(
                                parameterWithName("token").description("이메일로 받은 인증 토큰")
                            ),
                            responseFields(
                                fieldWithPath("success").description("요청 성공 여부")
                            )
                        )
                    )
                )
        }
        
        test("회원가입 인증 메일 확인 API 실패 테스트 - 유효하지 않은 토큰") {
            // Given
            val token = "invalid_verification_token"
            
            every { registerUserService.verifyCreateUser(any(), any()) } throws CoreException(Error.NOT_FOUND)
            
            // When & Then
            mockMvc.perform(
                get("/api/user/register/verify")
                    .param("token", token)
            )
                .andExpect(status().isNotFound)
                .andDo(
                    document(
                        "회원가입 인증 메일 확인 실패 - 유효하지 않은 토큰",
                        resourceDetails = ResourceSnippetParametersBuilder()
                            .summary("회원가입 인증 메일 확인")
                            .description("회원가입 인증 메일의 토큰을 확인하여 계정을 활성화합니다.")
                            .tag("user-register"),
                        snippets = arrayOf(
                            queryParameters(
                                parameterWithName("token").description("이메일로 받은 인증 토큰")
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