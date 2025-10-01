package com.eager.questioncloud.payment.coupon.controller

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import com.eager.questioncloud.payment.coupon.dto.RegisterCouponRequest
import com.eager.questioncloud.payment.coupon.service.UserCouponService
import com.eager.questioncloud.payment.dto.AvailableUserCoupon
import com.eager.questioncloud.payment.scenario.CouponScenario
import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document
import com.epages.restdocs.apispec.ResourceSnippetParametersBuilder
import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.kotest.core.extensions.ApplyExtension
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
import io.mockk.every
import io.mockk.justRun
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@ApplyExtension(SpringExtension::class)
class UserCouponControllerDocument(
    @Autowired private val mockMvc: MockMvc,
    @Autowired private val objectMapper: ObjectMapper
) : BehaviorSpec() {
    @MockkBean
    private lateinit var userCouponService: UserCouponService
    
    init {
        Given("사용 가능한 쿠폰 목록을 조회할 때") {
            When("사용자가 보유한 쿠폰 목록 조회를 요청하면") {
                val sampleCoupons = (1L..10L).map {
                    val coupon = CouponScenario.paymentFixedCoupon()
                    AvailableUserCoupon(
                        it,
                        coupon.title,
                        coupon.couponType,
                        coupon.discountCalculationType,
                        coupon.targetQuestionId,
                        coupon.targetCreatorId,
                        coupon.targetCategoryId,
                        coupon.minimumPurchaseAmount,
                        coupon.maximumDiscountAmount,
                        coupon.isDuplicable,
                        coupon.value,
                        coupon.endAt
                    )
                }
                
                every { userCouponService.getAvailableUserCoupons(any()) } returns sampleCoupons
                
                Then("사용 가능한 쿠폰 목록이 반환된다") {
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
                                        fieldWithPath("coupons[].couponType").description("쿠폰 종류"),
                                        fieldWithPath("coupons[].discountCalculationType").description("할인 방식"),
                                        fieldWithPath("coupons[].targetQuestionId").description("쿠폰 대상 문제").optional(),
                                        fieldWithPath("coupons[].targetCreatorId").description("쿠폰 대상 크리에이터").optional(),
                                        fieldWithPath("coupons[].targetCategoryId").description("쿠폰 대상 과목").optional(),
                                        fieldWithPath("coupons[].minimumPurchaseAmount").description("최소 주문 금액"),
                                        fieldWithPath("coupons[].maximumDiscountAmount").description("최대 할인 금액"),
                                        fieldWithPath("coupons[].isDuplicable").description("중복 가능 여부"),
                                        fieldWithPath("coupons[].value").description("쿠폰 값 (할인율 또는 할인금액)"),
                                        fieldWithPath("coupons[].endAt").description("쿠폰 만료일시")
                                    )
                                )
                            )
                        )
                }
            }
        }
        
        Given("유효한 쿠폰 코드로 등록을 시도할 때") {
            When("올바른 쿠폰 코드로 등록 요청을 하면") {
                val registerCouponRequest = RegisterCouponRequest(
                    code = "WELCOME2024"
                )
                
                justRun { userCouponService.registerCoupon(any(), any()) }
                
                Then("쿠폰 등록이 성공한다") {
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
            }
        }
        
        Given("존재하지 않는 쿠폰 코드로 등록을 시도할 때") {
            When("잘못된 쿠폰 코드로 등록 요청을 하면") {
                val registerCouponRequest = RegisterCouponRequest(
                    code = "INVALID_CODE"
                )
                
                every { userCouponService.registerCoupon(any(), any()) } throws
                        CoreException(Error.NOT_FOUND)
                
                Then("NOT_FOUND 예외가 발생한다") {
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
            }
        }
        
        Given("이미 등록한 쿠폰으로 재등록을 시도할 때") {
            When("중복 등록 요청을 하면") {
                val registerCouponRequest = RegisterCouponRequest(
                    code = "WELCOME2024"
                )
                
                every { userCouponService.registerCoupon(any(), any()) } throws
                        CoreException(Error.ALREADY_REGISTER_COUPON)
                
                Then("ALREADY_REGISTER_COUPON 예외가 발생한다") {
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
            }
        }
        
        Given("수량이 부족한 쿠폰으로 등록을 시도할 때") {
            When("품절된 쿠폰으로 등록 요청을 하면") {
                val registerCouponRequest = RegisterCouponRequest(
                    code = "LIMITED_COUPON"
                )
                
                every { userCouponService.registerCoupon(any(), any()) } throws
                        CoreException(Error.LIMITED_COUPON)
                
                Then("LIMITED_COUPON 예외가 발생한다") {
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
        }
    }
}