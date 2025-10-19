package com.eager.questioncloud.payment.question.document

import com.eager.questioncloud.application.security.JwtAuthenticationFilter
import com.eager.questioncloud.filter.FilterExceptionHandlerFilter
import com.eager.questioncloud.payment.domain.*
import com.eager.questioncloud.payment.enums.CouponType
import com.eager.questioncloud.payment.question.controller.QuestionPaymentController
import com.eager.questioncloud.payment.question.dto.QuestionOrderRequest
import com.eager.questioncloud.payment.question.dto.QuestionPaymentRequest
import com.eager.questioncloud.payment.question.service.QuestionPaymentHistoryService
import com.eager.questioncloud.payment.question.service.QuestionPaymentService
import com.eager.questioncloud.payment.scenario.QuestionOrderScenario
import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document
import com.epages.restdocs.apispec.ResourceSnippetParametersBuilder
import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.kotest.core.extensions.ApplyExtension
import io.kotest.core.spec.style.FunSpec
import io.kotest.extensions.spring.SpringExtension
import io.mockk.every
import org.springframework.beans.factory.annotation.Autowired
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
import java.time.LocalDateTime

@WebMvcTest(
    controllers = [QuestionPaymentController::class],
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
class QuestionPaymentControllerDocument : FunSpec() {
    @Autowired
    private lateinit var mockMvc: MockMvc
    
    @Autowired
    private lateinit var objectMapper: ObjectMapper
    
    @MockkBean
    private lateinit var questionPaymentService: QuestionPaymentService
    
    @MockkBean
    private lateinit var questionPaymentHistoryService: QuestionPaymentHistoryService
    
    init {
        val sampleQuestionPaymentHistory = listOf(
            QuestionPaymentHistory(
                orderId = "order-question-123456",
                userId = 1L,
                orders = listOf(
                    QuestionPaymentHistoryOrder(
                        orderItemId = 1L,
                        questionId = 101L,
                        originalPrice = 30000,
                        realPrice = 27000,
                        promotionName = "얼리버드 할인",
                        promotionDiscountAmount = 3000,
                        title = "미적분 킬러",
                        thumbnail = "https://example.com/kotlin.jpg",
                        creatorName = "김미적",
                        subject = "수학",
                        mainCategory = "미적분",
                        subCategory = "미분"
                    )
                ),
                discountInformation = listOf(
                    SimpleDiscountHistory(
                        name = "신규 가입 쿠폰",
                        couponType = CouponType.PAYMENT,
                        orderItemId = null,
                        discountAmount = 5000
                    )
                ),
                originalAmount = 30000,
                realAmount = 22000,
                createdAt = LocalDateTime.of(2024, 3, 15, 14, 30)
            ),
            QuestionPaymentHistory(
                orderId = "order-question-789012",
                userId = 1L,
                orders = listOf(
                    QuestionPaymentHistoryOrder(
                        orderItemId = 2L,
                        questionId = 102L,
                        originalPrice = 50000,
                        realPrice = 50000,
                        promotionName = null,
                        promotionDiscountAmount = 0,
                        title = "미적분 킬러 2",
                        thumbnail = "https://example.com/kotlin.jpg",
                        creatorName = "김미적",
                        subject = "수학",
                        mainCategory = "미적분",
                        subCategory = "미분"
                    )
                ),
                discountInformation = emptyList(),
                originalAmount = 50000,
                realAmount = 50000,
                createdAt = LocalDateTime.of(2024, 3, 10, 9, 15)
            )
        )
        
        test("문제 결제 요청 API") {
            // Given
            val paymentRequest = QuestionPaymentRequest(
                orders = listOf(
                    QuestionOrderRequest(
                        questionId = 101L,
                        orderUserCouponId = 1L,
                        duplicableOrderUserCouponId = null
                    )
                ),
                paymentUserCouponId = 2L
            )
            val questionOrderScenairo = QuestionOrderScenario.create(2)
            
            every { questionPaymentService.payment(any()) } returns QuestionPayment.create(
                userId = 1L,
                order = QuestionOrder.createOrder(questionOrderScenairo.items)
            )
            
            // When & Then
            mockMvc.perform(
                post("/api/payment/question")
                    .header("Authorization", "Bearer mock_access_token")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(paymentRequest))
            )
                .andExpect(status().isOk)
                .andDo(
                    document(
                        "문제 결제 요청",
                        resourceDetails = ResourceSnippetParametersBuilder()
                            .summary("문제 결제 요청")
                            .description("포인트를 사용하여 문제를 결제합니다. 쿠폰 적용도 가능합니다.")
                            .tag("payment"),
                        snippets = arrayOf(
                            requestFields(
                                fieldWithPath("orders").description("주문 목록"),
                                fieldWithPath("orders[].questionId").description("문제 ID"),
                                fieldWithPath("orders[].orderUserCouponId").description("문제 쿠폰 ID (선택)"),
                                fieldWithPath("orders[].duplicableOrderUserCouponId").description("중복 가능 문제 쿠폰 ID (선택)"),
                                fieldWithPath("paymentUserCouponId").description("결제 쿠폰 ID (선택)")
                            ),
                            responseFields(
                                fieldWithPath("success").description("결제 성공 여부")
                            )
                        )
                    )
                )
        }
        
        test("질문지 결제 내역 조회 API") {
            // Given
            val totalCount = 15
            
            every { questionPaymentHistoryService.countQuestionPaymentHistory(any()) } returns totalCount
            every { questionPaymentHistoryService.getQuestionPaymentHistory(any(), any()) } returns sampleQuestionPaymentHistory
            
            // When & Then
            mockMvc.perform(
                get("/api/payment/question/history")
                    .header("Authorization", "Bearer mock_access_token")
                    .param("page", "1")
                    .param("size", "10")
            )
                .andExpect(status().isOk)
                .andDo(
                    document(
                        "문제 결제 내역 조회",
                        resourceDetails = ResourceSnippetParametersBuilder()
                            .summary("문제 결제 내역 조회")
                            .description("사용자의 문제 결제 내역을 페이징하여 조회합니다.")
                            .tag("payment"),
                        snippets = arrayOf(
                            queryParameters(
                                parameterWithName("page").description("페이지 번호 (1부터 시작)"),
                                parameterWithName("size").description("페이지당 항목 수")
                            ),
                            responseFields(
                                fieldWithPath("total").description("전체 결제 내역 수"),
                                fieldWithPath("result").description("결제 내역 목록"),
                                fieldWithPath("result[].orderId").description("주문 ID"),
                                fieldWithPath("result[].userId").description("사용자 ID"),
                                fieldWithPath("result[].orders").description("주문 항목 목록"),
                                fieldWithPath("result[].orders[].orderItemId").description("주문 항목 ID"),
                                fieldWithPath("result[].orders[].questionId").description("문제 ID"),
                                fieldWithPath("result[].orders[].originalPrice").description("원가"),
                                fieldWithPath("result[].orders[].realPrice").description("실제 결제 금액"),
                                fieldWithPath("result[].orders[].promotionName").description("프로모션 이름 (없으면 null)").optional(),
                                fieldWithPath("result[].orders[].promotionDiscountAmount").description("프로모션 할인 금액").optional(),
                                fieldWithPath("result[].orders[].title").description("문제 제목"),
                                fieldWithPath("result[].orders[].thumbnail").description("썸네일 이미지 URL"),
                                fieldWithPath("result[].orders[].creatorName").description("크리에이터 이름"),
                                fieldWithPath("result[].orders[].subject").description("과목"),
                                fieldWithPath("result[].orders[].mainCategory").description("메인 카테고리"),
                                fieldWithPath("result[].orders[].subCategory").description("서브 카테고리"),
                                fieldWithPath("result[].discountInformation").description("할인 정보 목록"),
                                fieldWithPath("result[].discountInformation[].name").description("쿠폰/할인 이름"),
                                fieldWithPath("result[].discountInformation[].couponType").description("쿠폰 타입 (PAYMENT, ORDER, DUPLICABLE_ORDER 중 하나, 없으면 null)"),
                                fieldWithPath("result[].discountInformation[].orderItemId").description("주문 항목 ID (전체 할인일 경우 null)"),
                                fieldWithPath("result[].discountInformation[].discountAmount").description("할인 금액"),
                                fieldWithPath("result[].originalAmount").description("전체 원가"),
                                fieldWithPath("result[].realAmount").description("실제 결제 금액"),
                                fieldWithPath("result[].createdAt").description("결제 일시")
                            )
                        )
                    )
                )
        }
    }
}