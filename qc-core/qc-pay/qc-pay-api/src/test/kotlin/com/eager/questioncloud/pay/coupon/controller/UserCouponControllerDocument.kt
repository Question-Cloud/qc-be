package com.eager.questioncloud.pay.coupon.controller

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import com.eager.questioncloud.pay.coupon.dto.RegisterCouponRequest
import com.eager.questioncloud.pay.coupon.service.UserCouponService
import com.eager.questioncloud.payment.dto.AvailableUserCoupon
import com.eager.questioncloud.payment.enums.CouponType
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
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDateTime

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@AutoConfigureRestDocs
class UserCouponControllerDocument {
    @Autowired
    private lateinit var mockMvc: MockMvc
    
    @Autowired
    private lateinit var objectMapper: ObjectMapper
    
    @MockBean
    private lateinit var userCouponService: UserCouponService
    
    private lateinit var sampleAvailableUserCoupons: List<AvailableUserCoupon>
    
    @BeforeEach
    fun setUp() {
        sampleAvailableUserCoupons = listOf(
            AvailableUserCoupon(
                id = 1L,
                title = "10% 할인 쿠폰",
                couponType = CouponType.Percent,
                value = 10,
                endAt = LocalDateTime.of(2024, 12, 31, 23, 59, 59)
            ),
            AvailableUserCoupon(
                id = 2L,
                title = "5000원 할인 쿠폰",
                couponType = CouponType.Fixed,
                value = 5000,
                endAt = LocalDateTime.of(2024, 6, 30, 23, 59, 59)
            ),
            AvailableUserCoupon(
                id = 3L,
                title = "신규가입 특별 할인",
                couponType = CouponType.Percent,
                value = 20,
                endAt = LocalDateTime.of(2024, 12, 25, 23, 59, 59)
            )
        )
    }
    
    @Test
    fun `사용 가능한 쿠폰 목록 조회 API 테스트`() {
        // Given
        whenever(userCouponService.getAvailableUserCoupons(any()))
            .thenReturn(sampleAvailableUserCoupons)
        
        // When & Then
        mockMvc.perform(
            get("/api/payment/coupon")
                .header("Authorization", "Bearer mock_access_token")
        )
            .andExpect(status().isOk)
            .andDo(
                document(
                    "사용 가능한 쿠폰 목록 조회",
                    resourceDetails = ResourceSnippetParametersBuilder()
                        .summary("사용 가능한 쿠폰 목록 조회")
                        .description("사용자가 보유한 사용 가능한 쿠폰 목록을 조회합니다.")
                        .tag("payment-coupon"),
                    snippets = arrayOf(
                        responseFields(
                            fieldWithPath("coupons").description("사용 가능한 쿠폰 목록"),
                            fieldWithPath("coupons[].id").description("쿠폰 ID"),
                            fieldWithPath("coupons[].title").description("쿠폰 제목"),
                            fieldWithPath("coupons[].couponType").description("쿠폰 타입 (Percent: 할인율, Fixed: 고정 할인금액)"),
                            fieldWithPath("coupons[].value").description("쿠폰 값 (할인율 또는 할인금액)"),
                            fieldWithPath("coupons[].endAt").description("쿠폰 만료일시")
                        )
                    )
                )
            )
    }
    
    @Test
    fun `쿠폰 등록 API 테스트`() {
        // Given
        val registerCouponRequest = RegisterCouponRequest(
            code = "WELCOME2024"
        )
        
        // When & Then
        mockMvc.perform(
            post("/api/payment/coupon")
                .header("Authorization", "Bearer mock_access_token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerCouponRequest))
        )
            .andExpect(status().isOk)
            .andDo(
                document(
                    "쿠폰 등록",
                    resourceDetails = ResourceSnippetParametersBuilder()
                        .summary("쿠폰 등록")
                        .description("쿠폰 코드를 입력하여 쿠폰을 등록합니다.")
                        .tag("payment-coupon"),
                    snippets = arrayOf(
                        requestFields(
                            fieldWithPath("code").description("등록할 쿠폰 코드")
                        ),
                        responseFields(
                            fieldWithPath("success").description("요청 성공 여부")
                        )
                    )
                )
            )
    }
    
    @Test
    fun `쿠폰 등록 API 실패 테스트 - 존재하지 않는 쿠폰`() {
        // Given
        val registerCouponRequest = RegisterCouponRequest(
            code = "INVALID_CODE"
        )
        
        whenever(userCouponService.registerCoupon(any(), any()))
            .thenThrow(CoreException(Error.NOT_FOUND))
        
        // When & Then
        mockMvc.perform(
            post("/api/payment/coupon")
                .header("Authorization", "Bearer mock_access_token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerCouponRequest))
        )
            .andExpect(status().isNotFound)
            .andDo(
                document(
                    "쿠폰 등록 실패 - 존재하지 않는 쿠폰",
                    resourceDetails = ResourceSnippetParametersBuilder()
                        .summary("쿠폰 등록")
                        .description("쿠폰 코드를 입력하여 쿠폰을 등록합니다.")
                        .tag("payment-coupon"),
                    snippets = arrayOf(
                        requestFields(
                            fieldWithPath("code").description("등록할 쿠폰 코드")
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
    fun `쿠폰 등록 API 실패 테스트 - 이미 등록한 쿠폰`() {
        // Given
        val registerCouponRequest = RegisterCouponRequest(
            code = "WELCOME2024"
        )
        
        whenever(userCouponService.registerCoupon(any(), any()))
            .thenThrow(CoreException(Error.ALREADY_REGISTER_COUPON))
        
        // When & Then
        mockMvc.perform(
            post("/api/payment/coupon")
                .header("Authorization", "Bearer mock_access_token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerCouponRequest))
        )
            .andExpect(status().isConflict)
            .andDo(
                document(
                    "쿠폰 등록 실패 - 이미 등록한 쿠폰",
                    resourceDetails = ResourceSnippetParametersBuilder()
                        .summary("쿠폰 등록")
                        .description("쿠폰 코드를 입력하여 쿠폰을 등록합니다.")
                        .tag("payment-coupon"),
                    snippets = arrayOf(
                        requestFields(
                            fieldWithPath("code").description("등록할 쿠폰 코드")
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
    fun `쿠폰 등록 API 실패 테스트 - 쿠폰 수량 부족`() {
        // Given
        val registerCouponRequest = RegisterCouponRequest(
            code = "LIMITED_COUPON"
        )
        
        whenever(userCouponService.registerCoupon(any(), any()))
            .thenThrow(CoreException(Error.LIMITED_COUPON))
        
        // When & Then
        mockMvc.perform(
            post("/api/payment/coupon")
                .header("Authorization", "Bearer mock_access_token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerCouponRequest))
        )
            .andExpect(status().isBadRequest)
            .andDo(
                document(
                    "쿠폰 등록 실패 - 쿠폰 수량 부족",
                    resourceDetails = ResourceSnippetParametersBuilder()
                        .summary("쿠폰 등록")
                        .description("쿠폰 코드를 입력하여 쿠폰을 등록합니다.")
                        .tag("payment-coupon"),
                    snippets = arrayOf(
                        requestFields(
                            fieldWithPath("code").description("등록할 쿠폰 코드")
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
