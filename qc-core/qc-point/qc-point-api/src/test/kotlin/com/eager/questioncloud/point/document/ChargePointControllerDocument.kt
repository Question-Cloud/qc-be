package com.eager.questioncloud.point.document

import com.eager.questioncloud.application.security.JwtAuthenticationFilter
import com.eager.questioncloud.filter.FilterExceptionHandlerFilter
import com.eager.questioncloud.point.controller.ChargePointController
import com.eager.questioncloud.point.dto.ChargePointOrderRequest
import com.eager.questioncloud.point.dto.ChargePointPaymentHistory
import com.eager.questioncloud.point.dto.ChargePointPaymentRequest
import com.eager.questioncloud.point.enums.ChargePointPaymentStatus
import com.eager.questioncloud.point.enums.ChargePointType
import com.eager.questioncloud.point.service.ChargePointPaymentHistoryService
import com.eager.questioncloud.point.service.ChargePointPaymentService
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
import org.springframework.restdocs.request.RequestDocumentation.*
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDateTime

@WebMvcTest(
    controllers = [ChargePointController::class],
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
class ChargePointControllerDocument : FunSpec() {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @MockkBean
    private lateinit var chargePointPaymentService: ChargePointPaymentService

    @MockkBean
    private lateinit var chargePointPaymentHistoryService: ChargePointPaymentHistoryService

    init {
        val sampleChargePointPayment = listOf(
            ChargePointPaymentHistory(
                orderId = "order-123456",
                chargePointType = ChargePointType.PackageB,
                paidAt = LocalDateTime.of(2024, 3, 15, 14, 30),
            ),
            ChargePointPaymentHistory(
                orderId = "order-789012",
                chargePointType = ChargePointType.PackageA,
                paidAt = LocalDateTime.of(2024, 3, 10, 9, 15)
            ),
            ChargePointPaymentHistory(
                orderId = "order-345678",
                chargePointType = ChargePointType.PackageC,
                paidAt = LocalDateTime.of(2024, 3, 15, 14, 30)
            )
        )

        test("포인트 충전 완료 여부 조회 API 테스트") {
            // Given
            val paymentId = "payment-123456"
            val isComplete = true

            every { chargePointPaymentService.isCompletePayment(any(), any()) } returns isComplete

            // When & Then
            mockMvc.perform(
                get("/api/payment/point/status/{paymentId}", paymentId)
                    .header("Authorization", "Bearer mock_access_token")
            )
                .andExpect(status().isOk)
                .andDo(
                    document(
                        "포인트 충전 완료 여부 조회",
                        resourceDetails = ResourceSnippetParametersBuilder()
                            .summary("포인트 충전 완료 여부 조회")
                            .description("포인트 충전 결제가 완료되었는지 확인합니다.")
                            .tag("point"),
                        snippets = arrayOf(
                            pathParameters(
                                parameterWithName("paymentId").description("확인할 결제 ID")
                            ),
                            responseFields(
                                fieldWithPath("isCompleted").description("결제 완료 여부")
                            )
                        )
                    )
                )
        }

        test("포인트 충전 주문 생성 API 테스트") {
            // Given
            val orderRequest = ChargePointOrderRequest(
                chargePointType = ChargePointType.PackageB
            )
            val orderId = "order-123456789"

            every { chargePointPaymentService.createOrder(any(), any()) } returns orderId

            // When & Then
            mockMvc.perform(
                post("/api/payment/point/order")
                    .header("Authorization", "Bearer mock_access_token")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(orderRequest))
            )
                .andExpect(status().isOk)
                .andDo(
                    document(
                        "포인트 충전 주문 생성",
                        resourceDetails = ResourceSnippetParametersBuilder()
                            .summary("포인트 충전 주문 생성")
                            .description("포인트 충전 주문을 생성합니다. 포트원 결제창 호출 전 필수로 요청해야 합니다.")
                            .tag("point"),
                        snippets = arrayOf(
                            requestFields(
                                fieldWithPath("chargePointType").description("충전할 포인트 패키지 타입 (PackageA: 1000원, PackageB: 5000원, PackageC: 10000원, PackageD: 30000원, PackageE: 50000원, PackageF: 100000원)")
                            ),
                            responseFields(
                                fieldWithPath("orderId").description("생성된 주문 ID")
                            )
                        )
                    )
                )
        }

        test("포인트 충전 결제 승인 요청 API 테스트") {
            // Given
            val paymentRequest = ChargePointPaymentRequest(
                orderId = "order-123456789"
            )

            every { chargePointPaymentService.approvePayment(any()) } returns ChargePointPaymentStatus.CHARGED

            // When & Then
            mockMvc.perform(
                post("/api/payment/point")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(paymentRequest))
            )
                .andExpect(status().isOk)
                .andDo(
                    document(
                        "포인트 충전 결제 승인 요청",
                        resourceDetails = ResourceSnippetParametersBuilder()
                            .summary("포인트 충전 결제 승인 요청")
                            .description("포인트 충전 결제 승인을 요청합니다.")
                            .tag("point"),
                        snippets = arrayOf(
                            requestFields(
                                fieldWithPath("orderId").description("승인할 주문 ID")
                            ),
                            responseFields(
                                fieldWithPath("result").description("요청 성공 여부")
                            )
                        )
                    )
                )
        }

        test("포인트 충전 내역 조회 API 테스트") {
            // Given
            val totalCount = 15

            every { chargePointPaymentHistoryService.countChargePointPayment(any()) } returns totalCount
            every { chargePointPaymentHistoryService.getChargePointPaymentHistory(any(), any()) } returns sampleChargePointPayment

            // When & Then
            mockMvc.perform(
                get("/api/payment/point/history")
                    .header("Authorization", "Bearer mock_access_token")
                    .param("page", "1")
                    .param("size", "10")
            )
                .andExpect(status().isOk)
                .andDo(
                    document(
                        "포인트 충전 내역 조회",
                        resourceDetails = ResourceSnippetParametersBuilder()
                            .summary("포인트 충전 내역 조회")
                            .description("사용자의 포인트 충전 내역을 페이징하여 조회합니다.")
                            .tag("point"),
                        snippets = arrayOf(
                            queryParameters(
                                parameterWithName("page").description("페이지 번호 (1부터 시작)"),
                                parameterWithName("size").description("페이지당 항목 수")
                            ),
                            responseFields(
                                fieldWithPath("total").description("전체 충전 내역 수"),
                                fieldWithPath("result").description("충전 내역 목록"),
                                fieldWithPath("result[].orderId").description("주문 ID"),
                                fieldWithPath("result[].chargePointType").description("충전한 포인트 패키지 타입"),
                                fieldWithPath("result[].paidAt").description("결제 완료일시 (미완료 시 null)")
                            )
                        )
                    )
                )
        }
    }
}