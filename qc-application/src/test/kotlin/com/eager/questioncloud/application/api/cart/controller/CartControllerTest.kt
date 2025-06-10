package com.eager.questioncloud.application.api.cart.controller

import com.eager.questioncloud.application.api.cart.dto.AddCartItemRequest
import com.eager.questioncloud.application.api.cart.dto.GetCartResponse
import com.eager.questioncloud.application.api.cart.dto.RemoveCartItemRequest
import com.eager.questioncloud.application.api.cart.service.CartService
import com.eager.questioncloud.application.api.common.DefaultResponse
import com.eager.questioncloud.core.domain.cart.dto.CartItemDetail
import com.eager.questioncloud.core.domain.question.enums.Subject
import com.eager.questioncloud.core.exception.CoreException
import com.eager.questioncloud.core.exception.Error
import com.epages.restdocs.apispec.ResourceSnippetParametersBuilder
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
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.restassured.RestAssuredRestDocumentation.documentationConfiguration
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class, RestDocumentationExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureRestDocs(outputDir = "build/generated-snippets")
@ActiveProfiles("test")
class CartControllerTest {
    @LocalServerPort
    private var port: Int = 0

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @MockBean
    private lateinit var cartService: CartService

    private lateinit var spec: RequestSpecification

    private lateinit var sampleCartItems: List<CartItemDetail>

    @BeforeEach
    fun setUp(restDocumentation: RestDocumentationContextProvider) {
        sampleCartItems = listOf(
            CartItemDetail(
                id = 1L,
                questionId = 101L,
                title = "수학 기본 문제집",
                thumbnail = "https://example.com/thumbnail1.jpg",
                creatorName = "김수학",
                subject = Subject.Mathematics,
                price = 15000
            ),
            CartItemDetail(
                id = 2L,
                questionId = 102L,
                title = "수학 기본 문제집2",
                thumbnail = "https://example.com/thumbnail2.jpg",
                creatorName = "김수학",
                subject = Subject.Mathematics,
                price = 20000
            )
        )

        spec = RequestSpecBuilder()
            .addFilter(documentationConfiguration(restDocumentation))
            .setContentType(ContentType.JSON)
            .addHeader("Authorization", "Bearer mock_access_token")
            .setPort(port)
            .build()
    }

    @Test
    fun `장바구니 조회 API 테스트`() {
        // Given
        whenever(cartService.getCartItems(any()))
            .thenReturn(sampleCartItems)

        // When & Then
        RestAssured.given(spec)
            .filter(
                document(
                    "장바구니 조회",
                    ResourceSnippetParametersBuilder()
                        .summary("장바구니 조회")
                        .description("사용자의 장바구니에 담긴 아이템 목록을 조회합니다.")
                        .tag("cart")
                        .responseFields(
                            fieldWithPath("success").description("요청 성공 여부"),
                            fieldWithPath("items").description("장바구니 아이템 목록"),
                            fieldWithPath("items[].id").description("장바구니 아이템 ID"),
                            fieldWithPath("items[].questionId").description("문제 ID"),
                            fieldWithPath("items[].title").description("문제 제목"),
                            fieldWithPath("items[].thumbnail").description("문제 썸네일 URL"),
                            fieldWithPath("items[].creatorName").description("문제 제작자명"),
                            fieldWithPath("items[].subject").description("문제 과목"),
                            fieldWithPath("items[].price").description("문제 가격")
                        )
                )
            )
            .`when`()
            .get("/api/cart")
            .then()
            .statusCode(200)
            .extract().response().`as`(GetCartResponse::class.java)
    }

    @Test
    fun `장바구니 담기 API 테스트`() {
        // Given
        val addCartItemRequest = AddCartItemRequest(questionId = 101L)

        // When & Then
        RestAssured.given(spec)
            .filter(
                document(
                    "장바구니 담기",
                    ResourceSnippetParametersBuilder()
                        .summary("장바구니 담기")
                        .description("문제를 사용자의 장바구니에 추가합니다.")
                        .tag("cart")
                        .requestFields(
                            fieldWithPath("questionId").description("장바구니에 담을 문제 ID")
                        )
                        .responseFields(
                            fieldWithPath("success").description("요청 성공 여부")
                        )
                )
            )
            .body(objectMapper.writeValueAsString(addCartItemRequest))
            .`when`()
            .post("/api/cart")
            .then()
            .statusCode(200)
            .extract().response().`as`(DefaultResponse::class.java)
    }

    @Test
    fun `장바구니 빼기 API 테스트`() {
        // Given
        val removeCartItemRequest = RemoveCartItemRequest(ids = listOf(1L, 2L))

        // When & Then
        RestAssured.given(spec)
            .filter(
                document(
                    "장바구니 빼기",
                    ResourceSnippetParametersBuilder()
                        .summary("장바구니 빼기")
                        .description("선택한 아이템들을 장바구니에서 제거합니다.")
                        .tag("cart")
                        .requestFields(
                            fieldWithPath("ids").description("제거할 장바구니 아이템 ID 목록")
                        )
                        .responseFields(
                            fieldWithPath("success").description("요청 성공 여부")
                        )
                )
            )
            .body(objectMapper.writeValueAsString(removeCartItemRequest))
            .`when`()
            .delete("/api/cart")
            .then()
            .statusCode(200)
            .extract().response().`as`(DefaultResponse::class.java)
    }

    @Test
    fun `장바구니 담기 API 실패 테스트 - 이용할 수 없는 문제`() {
        // Given
        val addCartItemRequest = AddCartItemRequest(questionId = 999L)

        whenever(cartService.appendCartItem(any(), any()))
            .thenThrow(CoreException(Error.UNAVAILABLE_QUESTION))

        // When & Then
        RestAssured.given(spec)
            .filter(
                document(
                    "장바구니 담기 실패 - 이용할 수 없는 문제",
                    ResourceSnippetParametersBuilder()
                        .summary("장바구니 담기")
                        .description("문제를 사용자의 장바구니에 추가합니다.")
                        .tag("cart")
                        .requestFields(
                            fieldWithPath("questionId").description("이용할 수 없는 문제 ID")
                        )
                        .responseFields(
                            fieldWithPath("status").description("HTTP 상태 코드"),
                            fieldWithPath("message").description("에러 메시지")
                        )
                )
            )
            .body(objectMapper.writeValueAsString(addCartItemRequest))
            .`when`()
            .post("/api/cart")
            .then()
            .statusCode(400)
    }

    @Test
    fun `장바구니 담기 API 실패 테스트 - 이미 장바구니에 있는 문제`() {
        // Given
        val addCartItemRequest = AddCartItemRequest(questionId = 101L)

        whenever(cartService.appendCartItem(any(), any()))
            .thenThrow(CoreException(Error.ALREADY_IN_CART))

        // When & Then
        RestAssured.given(spec)
            .filter(
                document(
                    "장바구니 담기 실패 - 이미 장바구니에 있는 문제",
                    ResourceSnippetParametersBuilder()
                        .summary("장바구니 담기")
                        .description("문제를 사용자의 장바구니에 추가합니다.")
                        .tag("cart")
                        .requestFields(
                            fieldWithPath("questionId").description("이미 장바구니에 담겨 있는 문제 ID")
                        )
                        .responseFields(
                            fieldWithPath("status").description("HTTP 상태 코드"),
                            fieldWithPath("message").description("에러 메시지")
                        )
                )
            )
            .body(objectMapper.writeValueAsString(addCartItemRequest))
            .`when`()
            .post("/api/cart")
            .then()
            .statusCode(409)
    }

    @Test
    fun `장바구니 담기 API 실패 테스트 - 이미 보유한 문제`() {
        // Given
        val addCartItemRequest = AddCartItemRequest(questionId = 101L)

        whenever(cartService.appendCartItem(any(), any()))
            .thenThrow(CoreException(Error.ALREADY_OWN_QUESTION))

        // When & Then
        RestAssured.given(spec)
            .filter(
                document(
                    "장바구니 담기 실패 - 이미 보유한 문제",
                    ResourceSnippetParametersBuilder()
                        .summary("장바구니 담기")
                        .description("문제를 사용자의 장바구니에 추가합니다.")
                        .tag("cart")
                        .requestFields(
                            fieldWithPath("questionId").description("이미 보유하고 있는 문제 ID")
                        )
                        .responseFields(
                            fieldWithPath("status").description("HTTP 상태 코드"),
                            fieldWithPath("message").description("에러 메시지")
                        )
                )
            )
            .body(objectMapper.writeValueAsString(addCartItemRequest))
            .`when`()
            .post("/api/cart")
            .then()
            .statusCode(409)
    }
}
