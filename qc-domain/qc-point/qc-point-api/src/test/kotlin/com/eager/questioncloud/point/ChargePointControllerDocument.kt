package com.eager.questioncloud.point

import com.eager.questioncloud.point.domain.ChargePointPayment
import com.eager.questioncloud.point.dto.ChargePointOrderRequest
import com.eager.questioncloud.point.dto.ChargePointPaymentRequest
import com.eager.questioncloud.point.enums.ChargePointPaymentStatus
import com.eager.questioncloud.point.enums.ChargePointType
import com.eager.questioncloud.point.service.ChargePointPaymentHistoryService
import com.eager.questioncloud.point.service.ChargePointPaymentService
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
import org.springframework.restdocs.request.RequestDocumentation.*
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDateTime

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@AutoConfigureRestDocs
class ChargePointControllerDocument {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @MockBean
    private lateinit var chargePointPaymentService: ChargePointPaymentService

    @MockBean
    private lateinit var chargePointPaymentHistoryService: ChargePointPaymentHistoryService

    private lateinit var sampleChargePointPayment: List<ChargePointPayment>

    @BeforeEach
    fun setUp() {
        sampleChargePointPayment = listOf(
            ChargePointPayment(
                orderId = "order-123456",
                paymentId = "payment-123456",
                userId = 1L,
                chargePointType = ChargePointType.PackageB,
                chargePointPaymentStatus = ChargePointPaymentStatus.CHARGED,
                createdAt = LocalDateTime.of(2024, 3, 15, 14, 30),
                requestAt = LocalDateTime.of(2024, 3, 15, 14, 30)
            ),
            ChargePointPayment(
                orderId = "order-789012",
                paymentId = "payment-789012",
                userId = 1L,
                chargePointType = ChargePointType.PackageA,
                chargePointPaymentStatus = ChargePointPaymentStatus.CHARGED,
                createdAt = LocalDateTime.of(2024, 3, 10, 9, 15),
                requestAt = LocalDateTime.of(2024, 3, 10, 9, 15)
            ),
            ChargePointPayment(
                orderId = "order-345678",
                paymentId = "payment-345678",
                userId = 1L,
                chargePointType = ChargePointType.PackageC,
                chargePointPaymentStatus = ChargePointPaymentStatus.CHARGED,
                createdAt = LocalDateTime.of(2024, 3, 15, 14, 30),
                requestAt = LocalDateTime.of(2024, 3, 15, 14, 30)
            )
        )
    }

    @Test
    fun `포인트 충전 완료 여부 조회 API 테스트`() {
        // Given
        val paymentId = "payment-123456"
        val isComplete = true

        whenever(chargePointPaymentService.isCompletePayment(any(), any()))
            .thenReturn(isComplete)

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
                        .tag("charge-point"),
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

    @Test
    fun `포인트 충전 주문 생성 API 테스트`() {
        // Given
        val orderRequest = ChargePointOrderRequest(
            chargePointType = ChargePointType.PackageB
        )
        val orderId = "order-123456789"

        whenever(chargePointPaymentService.createOrder(any(), any()))
            .thenReturn(orderId)

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
                        .tag("charge-point"),
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

    @Test
    fun `포인트 충전 결제 승인 요청 API 테스트`() {
        // Given
        val paymentRequest = ChargePointPaymentRequest(
            orderId = "order-123456789"
        )

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
                        .tag("charge-point"),
                    snippets = arrayOf(
                        requestFields(
                            fieldWithPath("orderId").description("승인할 주문 ID")
                        ),
                        responseFields(
                            fieldWithPath("success").description("요청 성공 여부")
                        )
                    )
                )
            )
    }

    @Test
    fun `포인트 충전 내역 조회 API 테스트`() {
        // Given
        val totalCount = 15

        whenever(chargePointPaymentHistoryService.countChargePointPayment(any()))
            .thenReturn(totalCount)
        whenever(chargePointPaymentHistoryService.getChargePointPayments(any(), any()))
            .thenReturn(sampleChargePointPayment)

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
                        .tag("charge-point"),
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
