package com.eager.questioncloud.application.api.authentication.controller

import com.eager.questioncloud.application.api.authentication.dto.*
import com.eager.questioncloud.application.api.authentication.service.AuthenticationService
import com.eager.questioncloud.core.exception.CoreException
import com.eager.questioncloud.core.exception.Error
import com.epages.restdocs.apispec.RestAssuredRestDocumentationWrapper.document
import com.fasterxml.jackson.databind.ObjectMapper
import io.restassured.RestAssured
import io.restassured.builder.RequestSpecBuilder
import io.restassured.http.ContentType
import io.restassured.specification.RequestSpecification
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.restdocs.operation.preprocess.Preprocessors.*
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.request.RequestDocumentation.queryParameters
import org.springframework.restdocs.restassured.RestAssuredRestDocumentation.documentationConfiguration
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class, RestDocumentationExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureRestDocs(outputDir = "build/generated-snippets")
@ActiveProfiles("test")
class AuthenticationControllerTest {
    @LocalServerPort
    private var port: Int = 0

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @MockBean
    private lateinit var authenticationService: AuthenticationService

    private lateinit var spec: RequestSpecification

    private lateinit var sampleAuthenticationToken: AuthenticationToken

    @BeforeEach
    fun setUp(restDocumentation: RestDocumentationContextProvider) {
        sampleAuthenticationToken = AuthenticationToken(
            accessToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c",
            refreshToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.refresh_token_example"
        )

        spec = RequestSpecBuilder()
            .addFilter(documentationConfiguration(restDocumentation))
            .setContentType(ContentType.JSON)
            .setPort(port)
            .build()
    }

    @Test
    fun `로그인 API 테스트`() {
        // Given
        val loginRequest = LoginRequest(
            email = "test@example.com",
            password = "password123!"
        )

        whenever(authenticationService.login(any(), any()))
            .thenReturn(sampleAuthenticationToken)

        // When & Then
        RestAssured.given(spec)
            .filter(
                document(
                    "로그인",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    requestFields(
                        fieldWithPath("email").description("사용자 이메일 주소"),
                        fieldWithPath("password").description("사용자 비밀번호")
                    ),
                    responseFields(
                        fieldWithPath("authenticationToken").description("인증 토큰 정보"),
                        fieldWithPath("authenticationToken.accessToken").description("액세스 토큰"),
                        fieldWithPath("authenticationToken.refreshToken").description("리프레시 토큰")
                    )
                )
            )
            .body(objectMapper.writeValueAsString(loginRequest))
            .`when`()
            .post("/api/auth")
            .then()
            .statusCode(200)
            .extract().response().`as`(LoginResponse::class.java)
    }

    @Test
    fun `토큰 리프레시 API 테스트`() {
        // Given
        val refreshToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.refresh_token_example"

        whenever(authenticationService.refresh(any()))
            .thenReturn(sampleAuthenticationToken)

        // When & Then
        RestAssured.given(spec)
            .filter(
                document(
                    "토큰 리프레시",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    queryParameters(
                        parameterWithName("refreshToken").description("리프레시 토큰")
                    ),
                    responseFields(
                        fieldWithPath("authenticationToken").description("새로 발급된 인증 토큰 정보"),
                        fieldWithPath("authenticationToken.accessToken").description("새 액세스 토큰"),
                        fieldWithPath("authenticationToken.refreshToken").description("새 리프레시 토큰")
                    )
                )
            )
            .queryParam("refreshToken", refreshToken)
            .`when`()
            .post("/api/auth/refresh")
            .then()
            .statusCode(200)
            .extract().response().`as`(RefreshResponse::class.java)
    }

    @Test
    fun `소셜 로그인 API 테스트 - 기존 사용자`() {
        // Given
        val socialAuthenticationResult = SocialAuthenticationResult.success(sampleAuthenticationToken)

        whenever(authenticationService.socialLogin(any(), any()))
            .thenReturn(socialAuthenticationResult)

        // When & Then
        RestAssured.given(spec)
            .filter(
                document(
                    "소셜 로그인 - 기존 사용자",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    queryParameters(
                        parameterWithName("accountType").description("소셜 계정 타입 (KAKAO, GOOGLE, NAVER)"),
                        parameterWithName("code").description("OAuth 인증 코드")
                    ),
                    responseFields(
                        fieldWithPath("isRegistered").description("기존 등록 사용자 여부"),
                        fieldWithPath("authenticationToken").description("인증 토큰 정보 (기존 사용자인 경우)").optional(),
                        fieldWithPath("authenticationToken.accessToken").description("액세스 토큰").optional(),
                        fieldWithPath("authenticationToken.refreshToken").description("리프레시 토큰").optional(),
                        fieldWithPath("registerToken").description("회원가입 토큰 (신규 사용자인 경우)").optional()
                            .type(JsonFieldType.STRING)
                    )
                )
            )
            .queryParam("accountType", "KAKAO")
            .queryParam("code", "test_authorization_code")
            .`when`()
            .get("/api/auth/social")
            .then()
            .statusCode(200)
            .extract().response().`as`(SocialAuthenticateResponse::class.java)
    }

    @Test
    fun `소셜 로그인 API 테스트 - 신규 사용자`() {
        // Given
        val socialAuthenticationResult = SocialAuthenticationResult.notRegister("register_token_for_new_user")

        whenever(authenticationService.socialLogin(any(), any()))
            .thenReturn(socialAuthenticationResult)

        // When & Then
        RestAssured.given(spec)
            .filter(
                document(
                    "소셜 로그인 - 신규 사용자",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    queryParameters(
                        parameterWithName("accountType").description("소셜 계정 타입 (KAKAO, GOOGLE, NAVER)"),
                        parameterWithName("code").description("OAuth 인증 코드")
                    ),
                    responseFields(
                        fieldWithPath("isRegistered").description("기존 등록 사용자 여부"),
                        fieldWithPath("authenticationToken").description("인증 토큰 정보 (기존 사용자인 경우)"),
                        fieldWithPath("registerToken").description("회원가입 토큰 (신규 사용자인 경우)").optional()
                            .type(JsonFieldType.STRING)
                    )
                )
            )
            .queryParam("accountType", "GOOGLE")
            .queryParam("code", "test_authorization_code")
            .`when`()
            .get("/api/auth/social")
            .then()
            .statusCode(200)
            .extract().response().`as`(SocialAuthenticateResponse::class.java)
    }

    @Test
    fun `로그인 API 실패 테스트 - 존재하지 않는 사용자`() {
        // Given
        val loginRequest = LoginRequest(
            email = "nonexistent@example.com",
            password = "password123!"
        )

        whenever(authenticationService.login("nonexistent@example.com", "password123!"))
            .thenThrow(CoreException(Error.FAIL_LOGIN))

        // When & Then
        RestAssured.given(spec)
            .filter(
                document(
                    "로그인 실패 - 존재하지 않는 사용자",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    requestFields(
                        fieldWithPath("email").description("존재하지 않는 사용자의 이메일 주소"),
                        fieldWithPath("password").description("사용자 비밀번호")
                    ),
                    responseFields(
                        fieldWithPath("status").description("HTTP 상태 코드"),
                        fieldWithPath("message").description("에러 메시지")
                    )
                )
            )
            .body(objectMapper.writeValueAsString(loginRequest))
            .`when`()
            .post("/api/auth")
            .then()
            .statusCode(401)
    }

    @Test
    fun `로그인 API 실패 테스트 - 잘못된 비밀번호`() {
        // Given
        val loginRequest = LoginRequest(
            email = "test@example.com",
            password = "wrongpassword"
        )

        whenever(authenticationService.login("test@example.com", "wrongpassword"))
            .thenThrow(CoreException(Error.FAIL_LOGIN))

        // When & Then
        RestAssured.given(spec)
            .filter(
                document(
                    "로그인 실패 - 잘못된 비밀번호",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    requestFields(
                        fieldWithPath("email").description("사용자 이메일 주소"),
                        fieldWithPath("password").description("잘못된 비밀번호")
                    ),
                    responseFields(
                        fieldWithPath("status").description("HTTP 상태 코드"),
                        fieldWithPath("message").description("에러 메시지")
                    )
                )
            )
            .body(objectMapper.writeValueAsString(loginRequest))
            .`when`()
            .post("/api/auth")
            .then()
            .statusCode(401)
    }

    @Test
    fun `로그인 API 실패 테스트 - 이메일 인증 미완료`() {
        // Given
        val loginRequest = LoginRequest(
            email = "unverified@example.com",
            password = "password123!"
        )

        whenever(authenticationService.login("unverified@example.com", "password123!"))
            .thenThrow(CoreException(Error.PENDING_EMAIL_VERIFICATION))

        // When & Then
        RestAssured.given(spec)
            .filter(
                document(
                    "로그인 실패 - 이메일 인증 미완료",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    requestFields(
                        fieldWithPath("email").description("이메일 인증이 완료되지 않은 사용자의 이메일 주소"),
                        fieldWithPath("password").description("사용자 비밀번호")
                    ),
                    responseFields(
                        fieldWithPath("status").description("HTTP 상태 코드"),
                        fieldWithPath("message").description("에러 메시지")
                    )
                )
            )
            .body(objectMapper.writeValueAsString(loginRequest))
            .`when`()
            .post("/api/auth")
            .then()
            .statusCode(403)
    }

    @Test
    fun `로그인 API 실패 테스트 - 탈퇴 또는 정지된 사용자`() {
        // Given
        val loginRequest = LoginRequest(
            email = "inactive@example.com",
            password = "password123!"
        )

        whenever(authenticationService.login("inactive@example.com", "password123!"))
            .thenThrow(CoreException(Error.NOT_ACTIVE_USER))

        // When & Then
        RestAssured.given(spec)
            .filter(
                document(
                    "로그인 실패 - 탈퇴 또는 정지된 사용자",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    requestFields(
                        fieldWithPath("email").description("탈퇴하거나 정지된 사용자의 이메일 주소"),
                        fieldWithPath("password").description("사용자 비밀번호")
                    ),
                    responseFields(
                        fieldWithPath("status").description("HTTP 상태 코드"),
                        fieldWithPath("message").description("에러 메시지")
                    )
                )
            )
            .body(objectMapper.writeValueAsString(loginRequest))
            .`when`()
            .post("/api/auth")
            .then()
            .statusCode(403)
    }

    @Test
    fun `토큰 리프레시 API 실패 테스트 - 유효하지 않은 리프레시 토큰`() {
        // Given
        val invalidRefreshToken = "invalid.refresh.token"

        whenever(authenticationService.refresh(invalidRefreshToken))
            .thenThrow(CoreException(Error.UNAUTHORIZED_TOKEN))

        // When & Then
        RestAssured.given(spec)
            .filter(
                document(
                    "토큰 리프레시 실패 - 유효하지 않은 리프레시 토큰",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    queryParameters(
                        parameterWithName("refreshToken").description("유효하지 않은 리프레시 토큰")
                    ),
                    responseFields(
                        fieldWithPath("status").description("HTTP 상태 코드"),
                        fieldWithPath("message").description("에러 메시지")
                    )
                )
            )
            .queryParam("refreshToken", invalidRefreshToken)
            .`when`()
            .post("/api/auth/refresh")
            .then()
            .statusCode(401)
    }
}
