package com.eager.questioncloud.store.cart.controller

import com.eager.questioncloud.api.cart.dto.AddCartItemRequest
import com.eager.questioncloud.api.cart.dto.RemoveCartItemRequest
import com.eager.questioncloud.api.cart.service.CartService
import com.eager.questioncloud.cart.dto.CartItemDetail
import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import com.eager.questioncloud.question.enums.Subject
import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document
import com.epages.restdocs.apispec.ResourceSnippetParametersBuilder
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@AutoConfigureRestDocs
class CartControllerDocument {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @MockBean
    private lateinit var cartService: CartService

    private lateinit var sampleCartItems: List<CartItemDetail>

    @BeforeEach
    fun setUp() {
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
                title = "물리 기본 문제집",
                thumbnail = "https://example.com/thumbnail2.jpg",
                creatorName = "이물리",
                subject = Subject.Physics,
                price = 20000
            )
        )
    }

    @Test
    fun `장바구니 조회 API 테스트`() {
        // Given
        whenever(cartService.getCartItemDetails(any()))
            .thenReturn(sampleCartItems)

        // When & Then
        mockMvc.perform(
            get("/api/store/cart")
                .header("Authorization", "Bearer mock_access_token")
        )
            .andExpect(status().isOk)
            .andDo(
                document(
                    "장바구니 조회",
                    resourceDetails = ResourceSnippetParametersBuilder()
                        .summary("장바구니 조회")
                        .description("사용자의 장바구니에 담긴 아이템 목록을 조회합니다.")
                        .tag("cart"),
                    snippets = arrayOf(
                        responseFields(
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
            )
    }

    @Test
    fun `장바구니 담기 API 테스트`() {
        // Given
        val addCartItemRequest = AddCartItemRequest(questionId = 101L)

        // When & Then
        mockMvc.perform(
            post("/api/store/cart")
                .header("Authorization", "Bearer mock_access_token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(addCartItemRequest))
        )
            .andExpect(status().isOk)
            .andDo(
                document(
                    "장바구니 담기",
                    resourceDetails = ResourceSnippetParametersBuilder()
                        .summary("장바구니 담기")
                        .description("문제를 사용자의 장바구니에 추가합니다.")
                        .tag("cart"),
                    snippets = arrayOf(
                        requestFields(
                            fieldWithPath("questionId").description("장바구니에 담을 문제 ID")
                        ),
                        responseFields(
                            fieldWithPath("success").description("요청 성공 여부")
                        )
                    )
                )
            )
    }


    @Test
    fun `장바구니 담기 API 실패 테스트 - 이용할 수 없는 문제`() {
        // Given
        val addCartItemRequest = AddCartItemRequest(questionId = 999L)

        whenever(cartService.appendCartItem(any(), any()))
            .thenThrow(CoreException(Error.UNAVAILABLE_QUESTION))

        // When & Then
        mockMvc.perform(
            post("/api/store/cart")
                .header("Authorization", "Bearer mock_access_token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(addCartItemRequest))
        )
            .andExpect(status().isBadRequest)
            .andDo(
                document(
                    "장바구니 담기 실패 - 이용할 수 없는 문제",
                    resourceDetails = ResourceSnippetParametersBuilder()
                        .summary("장바구니 담기")
                        .description("문제를 사용자의 장바구니에 추가합니다.")
                        .tag("cart"),
                    snippets = arrayOf(
                        requestFields(
                            fieldWithPath("questionId").description("장바구니에 담을 문제 ID")
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
    fun `장바구니 담기 API 실패 테스트 - 이미 장바구니에 있는 문제`() {
        // Given
        val addCartItemRequest = AddCartItemRequest(questionId = 101L)

        whenever(cartService.appendCartItem(any(), any()))
            .thenThrow(CoreException(Error.ALREADY_IN_CART))

        // When & Then
        mockMvc.perform(
            post("/api/store/cart")
                .header("Authorization", "Bearer mock_access_token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(addCartItemRequest))
        )
            .andExpect(status().isConflict)
            .andDo(
                document(
                    "장바구니 담기 실패 - 이미 장바구니에 있는 문제",
                    resourceDetails = ResourceSnippetParametersBuilder()
                        .summary("장바구니 담기")
                        .description("문제를 사용자의 장바구니에 추가합니다.")
                        .tag("cart"),
                    snippets = arrayOf(
                        requestFields(
                            fieldWithPath("questionId").description("장바구니에 담을 문제 ID")
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
    fun `장바구니 담기 API 실패 테스트 - 이미 보유한 문제`() {
        // Given
        val addCartItemRequest = AddCartItemRequest(questionId = 101L)

        whenever(cartService.appendCartItem(any(), any()))
            .thenThrow(CoreException(Error.ALREADY_OWN_QUESTION))

        // When & Then
        mockMvc.perform(
            post("/api/store/cart")
                .header("Authorization", "Bearer mock_access_token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(addCartItemRequest))
        )
            .andExpect(status().isConflict)
            .andDo(
                document(
                    "장바구니 담기 실패 - 이미 보유한 문제",
                    resourceDetails = ResourceSnippetParametersBuilder()
                        .summary("장바구니 담기")
                        .description("문제를 사용자의 장바구니에 추가합니다.")
                        .tag("cart"),
                    snippets = arrayOf(
                        requestFields(
                            fieldWithPath("questionId").description("장바구니에 담을 문제 ID")
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
    fun `장바구니 빼기 API 테스트`() {
        // Given
        val removeCartItemRequest = RemoveCartItemRequest(ids = listOf(1L, 2L))

        // When & Then
        mockMvc.perform(
            delete("/api/store/cart")
                .header("Authorization", "Bearer mock_access_token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(removeCartItemRequest))
        )
            .andExpect(status().isOk)
            .andDo(
                document(
                    "장바구니 빼기",
                    resourceDetails = ResourceSnippetParametersBuilder()
                        .summary("장바구니 빼기")
                        .description("선택한 아이템들을 장바구니에서 제거합니다.")
                        .tag("cart"),
                    snippets = arrayOf(
                        requestFields(
                            fieldWithPath("ids").description("제거할 장바구니 아이템 ID 목록")
                        ),
                        responseFields(
                            fieldWithPath("success").description("요청 성공 여부")
                        )
                    )
                )
            )
    }
}
