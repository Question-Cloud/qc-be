package com.eager.questioncloud.cart.controller

import com.eager.questioncloud.application.security.JwtAuthenticationFilter
import com.eager.questioncloud.cart.dto.AddCartItemRequest
import com.eager.questioncloud.cart.dto.CartItemDetail
import com.eager.questioncloud.cart.dto.RemoveCartItemRequest
import com.eager.questioncloud.cart.service.CartService
import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document
import com.epages.restdocs.apispec.ResourceSnippetParametersBuilder
import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.kotest.core.extensions.ApplyExtension
import io.kotest.core.spec.style.FunSpec
import io.kotest.extensions.spring.SpringExtension
import io.mockk.every
import io.mockk.justRun
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.FilterType
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(
    controllers = [CartController::class],
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
class CartControllerDocument(
    @Autowired private val mockMvc: MockMvc,
    @Autowired private val objectMapper: ObjectMapper,
) : FunSpec() {
    
    @MockkBean
    private lateinit var cartService: CartService
    
    private val sampleCartItems = listOf(
        CartItemDetail(
            id = 1L,
            questionId = 101L,
            title = "수학 기본 문제집",
            thumbnail = "https://example.com/thumbnail1.jpg",
            creatorName = "김수학",
            subject = "Mathematics",
            price = 15000
        ),
        CartItemDetail(
            id = 2L,
            questionId = 102L,
            title = "물리 기본 문제집",
            thumbnail = "https://example.com/thumbnail2.jpg",
            creatorName = "이물리",
            subject = "Physics",
            price = 20000
        )
    )
    
    init {
        test("장바구니 조회 API 테스트") {
            every { cartService.getCartItemDetails(any()) } returns sampleCartItems
            
            mockMvc.perform(
                get("/api/store/cart")
                    .header("Authorization", "Bearer mock_access_token")
                    .with(csrf())
            )
                .andExpect(status().isOk)
                .andDo(
                    document(
                        "장바구니 조회",
                        resourceDetails = ResourceSnippetParametersBuilder()
                            .summary("장바구니 조회")
                            .description("사용자의 장바구니에 담긴 아이템 목록을 조회합니다.")
                            .tag("store-cart"),
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
        
        test("장바구니 담기 API 테스트") {
            val addCartItemRequest = AddCartItemRequest(questionId = 101L)
            justRun { cartService.appendCartItem(any(), any()) }
            
            mockMvc.perform(
                post("/api/store/cart")
                    .header("Authorization", "Bearer mock_access_token")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(addCartItemRequest))
                    .with(csrf())
            )
                .andExpect(status().isOk)
                .andDo(
                    document(
                        "장바구니 담기",
                        resourceDetails = ResourceSnippetParametersBuilder()
                            .summary("장바구니 담기")
                            .description("문제를 사용자의 장바구니에 추가합니다.")
                            .tag("store-cart"),
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
        
        test("장바구니 담기 API 실패 테스트 - 이용할 수 없는 문제") {
            val addCartItemRequest = AddCartItemRequest(questionId = 999L)
            
            every { cartService.appendCartItem(any(), any()) } throws CoreException(Error.UNAVAILABLE_QUESTION)
            
            mockMvc.perform(
                post("/api/store/cart")
                    .header("Authorization", "Bearer mock_access_token")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(addCartItemRequest))
                    .with(csrf())
            )
                .andExpect(status().isBadRequest)
                .andDo(
                    document(
                        "장바구니 담기 실패 - 이용할 수 없는 문제",
                        resourceDetails = ResourceSnippetParametersBuilder()
                            .summary("장바구니 담기")
                            .description("문제를 사용자의 장바구니에 추가합니다.")
                            .tag("store-cart"),
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
        
        test("장바구니 빼기 API 테스트") {
            val removeCartItemRequest = RemoveCartItemRequest(ids = listOf(1L, 2L))
            
            justRun { cartService.removeCartItem(any(), any()) }
            
            mockMvc.perform(
                delete("/api/store/cart")
                    .header("Authorization", "Bearer mock_access_token")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(removeCartItemRequest))
                    .with(csrf())
            )
                .andExpect(status().isOk)
                .andDo(
                    document(
                        "장바구니 빼기",
                        resourceDetails = ResourceSnippetParametersBuilder()
                            .summary("장바구니 빼기")
                            .description("선택한 아이템들을 장바구니에서 제거합니다.")
                            .tag("store-cart"),
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
}
