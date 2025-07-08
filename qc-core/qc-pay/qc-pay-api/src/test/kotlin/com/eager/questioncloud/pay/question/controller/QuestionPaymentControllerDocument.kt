package com.eager.questioncloud.pay.question.controller

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import com.eager.questioncloud.pay.question.dto.QuestionPaymentRequest
import com.eager.questioncloud.pay.question.service.QuestionOrderService
import com.eager.questioncloud.pay.question.service.QuestionPaymentCouponService
import com.eager.questioncloud.pay.question.service.QuestionPaymentHistoryService
import com.eager.questioncloud.pay.question.service.QuestionPaymentService
import com.eager.questioncloud.payment.domain.QuestionPaymentCoupon
import com.eager.questioncloud.payment.domain.QuestionPaymentHistory
import com.eager.questioncloud.payment.domain.QuestionPaymentHistoryOrder
import com.eager.questioncloud.payment.enums.CouponType
import com.eager.questioncloud.payment.enums.QuestionPaymentStatus
import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document
import com.epages.restdocs.apispec.ResourceSnippetParametersBuilder
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.request.RequestDocumentation.queryParameters
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDateTime

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@AutoConfigureRestDocs
class QuestionPaymentControllerDocument {
    @Autowired
    private lateinit var mockMvc: MockMvc
    
    @Autowired
    private lateinit var objectMapper: ObjectMapper
    
    @MockBean
    private lateinit var questionPaymentHistoryService: QuestionPaymentHistoryService
    
    @MockBean
    private lateinit var questionPaymentService: QuestionPaymentService
    
    @MockBean
    private lateinit var questionOrderService: QuestionOrderService
    
    @MockBean
    private lateinit var questionPaymentCouponService: QuestionPaymentCouponService
    
    private lateinit var sampleQuestionPaymentHistories: List<QuestionPaymentHistory>
    
    @BeforeEach
    fun setUp() {
        val sampleOrders = listOf(
            QuestionPaymentHistoryOrder(
                questionId = 101L,
                amount = 5000,
                title = "미적분 기본 문제집",
                thumbnail = "https://example.com/thumbnail1.jpg",
                creatorName = "김수학",
                subject = "Mathematics",
                mainCategory = "미적분",
                subCategory = "극한"
            ),
            QuestionPaymentHistoryOrder(
                questionId = 102L,
                amount = 3000,
                title = "물리 역학 문제집",
                thumbnail = "https://example.com/thumbnail2.jpg",
                creatorName = "이물리",
                subject = "Physics",
                mainCategory = "역학",
                subCategory = "운동"
            )
        )
        
        val sampleCoupon = QuestionPaymentCoupon(
            userCouponId = 1L,
            couponId = 1L,
            title = "10% 할인 쿠폰",
            couponType = CouponType.Percent,
            value = 10
        )
        
        sampleQuestionPaymentHistories = listOf(
            QuestionPaymentHistory(
                orderId = "order-123456",
                userId = 1L,
                orders = sampleOrders,
                coupon = sampleCoupon,
                amount = 7200, // 8000 - 10% 할인
                isUsedCoupon = true,
                status = QuestionPaymentStatus.SUCCESS,
                createdAt = LocalDateTime.of(2024, 3, 15, 14, 30)
            ),
            QuestionPaymentHistory(
                orderId = "order-789012",
                userId = 1L,
                orders = listOf(sampleOrders[0]),
                coupon = null,
                amount = 5000,
                isUsedCoupon = false,
                status = QuestionPaymentStatus.SUCCESS,
                createdAt = LocalDateTime.of(2024, 3, 10, 9, 15)
            )
        )
    }
    
    @Test
    fun `문제 구매 API 테스트`() {
        // Given
        val questionPaymentRequest = QuestionPaymentRequest(
            questionIds = listOf(101L, 102L),
            userCouponId = 1L
        )
        
        // When & Then
        mockMvc.perform(
            post("/api/payment/question")
                .header("Authorization", "Bearer mock_access_token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(questionPaymentRequest))
        )
            .andExpect(status().isOk)
            .andDo(
                document(
                    "문제 구매",
                    resourceDetails = ResourceSnippetParametersBuilder()
                        .summary("문제 구매")
                        .description("선택한 문제들을 구매합니다. 쿠폰을 사용할 수 있습니다.")
                        .tag("question-payment"),
                    snippets = arrayOf(
                        requestFields(
                            fieldWithPath("questionIds").description("구매할 문제 ID 목록"),
                            fieldWithPath("userCouponId").description("사용할 쿠폰 ID (선택사항, null 가능)")
                                .optional().type(JsonFieldType.NUMBER)
                        ),
                        responseFields(
                            fieldWithPath("success").description("요청 성공 여부")
                        )
                    )
                )
            )
    }
    
    @Test
    fun `문제 구매 API 실패 테스트 - 이미 구매한 문제`() {
        // Given
        val questionPaymentRequest = QuestionPaymentRequest(
            questionIds = listOf(101L),
            userCouponId = null
        )
        
        whenever(questionOrderService.generateQuestionOrder(any(), any()))
            .thenThrow(CoreException(Error.ALREADY_OWN_QUESTION))
        
        // When & Then
        mockMvc.perform(
            post("/api/payment/question")
                .header("Authorization", "Bearer mock_access_token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(questionPaymentRequest))
        )
            .andExpect(status().isConflict)
            .andDo(
                document(
                    "문제 구매 실패 - 이미 구매한 문제",
                    resourceDetails = ResourceSnippetParametersBuilder()
                        .summary("문제 구매")
                        .description("선택한 문제들을 구매합니다. 쿠폰을 사용할 수 있습니다.")
                        .tag("question-payment"),
                    snippets = arrayOf(
                        requestFields(
                            fieldWithPath("questionIds").description("구매할 문제 ID 목록"),
                            fieldWithPath("userCouponId").description("사용할 쿠폰 ID (null)")
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
    fun `문제 구매 API 실패 테스트 - 사용할 수 없는 문제`() {
        // Given
        val questionPaymentRequest = QuestionPaymentRequest(
            questionIds = listOf(999L, 1000L),
            userCouponId = null
        )
        
        whenever(questionOrderService.generateQuestionOrder(any(), any()))
            .thenThrow(CoreException(Error.UNAVAILABLE_QUESTION))
        
        // When & Then
        mockMvc.perform(
            post("/api/payment/question")
                .header("Authorization", "Bearer mock_access_token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(questionPaymentRequest))
        )
            .andExpect(status().isBadRequest)
            .andDo(
                document(
                    "문제 구매 실패 - 사용할 수 없는 문제",
                    resourceDetails = ResourceSnippetParametersBuilder()
                        .summary("문제 구매")
                        .description("선택한 문제들을 구매합니다. 쿠폰을 사용할 수 있습니다.")
                        .tag("question-payment"),
                    snippets = arrayOf(
                        requestFields(
                            fieldWithPath("questionIds").description("구매할 문제 ID 목록"),
                            fieldWithPath("userCouponId").description("사용할 쿠폰 ID (null)")
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
    fun `문제 구매 API 실패 테스트 - 포인트 부족`() {
        // Given
        val questionPaymentRequest = QuestionPaymentRequest(
            questionIds = listOf(101L, 102L),
            userCouponId = null
        )
        
        whenever(questionPaymentService.payment(anyOrNull(), anyOrNull(), anyOrNull()))
            .thenThrow(CoreException(Error.NOT_ENOUGH_POINT))
        
        // When & Then
        mockMvc.perform(
            post("/api/payment/question")
                .header("Authorization", "Bearer mock_access_token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(questionPaymentRequest))
        )
            .andExpect(status().isForbidden)
            .andDo(
                document(
                    "문제 구매 실패 - 포인트 부족",
                    resourceDetails = ResourceSnippetParametersBuilder()
                        .summary("문제 구매")
                        .description("선택한 문제들을 구매합니다. 쿠폰을 사용할 수 있습니다.")
                        .tag("question-payment"),
                    snippets = arrayOf(
                        requestFields(
                            fieldWithPath("questionIds").description("구매할 문제 ID 목록"),
                            fieldWithPath("userCouponId").description("사용할 쿠폰 ID (null)")
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
    fun `문제 구매 내역 조회 API 테스트`() {
        // Given
        val totalCount = 25
        
        whenever(questionPaymentHistoryService.countQuestionPaymentHistory(any()))
            .thenReturn(totalCount)
        whenever(questionPaymentHistoryService.getQuestionPaymentHistory(any(), any()))
            .thenReturn(sampleQuestionPaymentHistories)
        
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
                    "문제 구매 내역 조회",
                    resourceDetails = ResourceSnippetParametersBuilder()
                        .summary("문제 구매 내역 조회")
                        .description("사용자의 문제 구매 내역을 페이징하여 조회합니다.")
                        .tag("question-payment"),
                    snippets = arrayOf(
                        queryParameters(
                            parameterWithName("page").description("페이지 번호 (1부터 시작)"),
                            parameterWithName("size").description("페이지당 항목 수")
                        ),
                        responseFields(
                            fieldWithPath("total").description("전체 구매 내역 수"),
                            fieldWithPath("result").description("구매 내역 목록"),
                            fieldWithPath("result[].orderId").description("주문 ID"),
                            fieldWithPath("result[].userId").description("사용자 ID"),
                            fieldWithPath("result[].orders").description("주문한 문제 목록"),
                            fieldWithPath("result[].orders[].questionId").description("문제 ID"),
                            fieldWithPath("result[].orders[].amount").description("문제 가격"),
                            fieldWithPath("result[].orders[].title").description("문제 제목"),
                            fieldWithPath("result[].orders[].thumbnail").description("문제 썸네일 URL"),
                            fieldWithPath("result[].orders[].creatorName").description("문제 출제자 이름"),
                            fieldWithPath("result[].orders[].subject").description("과목 (Mathematics, Physics, Chemistry, Biology, EarthScience)"),
                            fieldWithPath("result[].orders[].mainCategory").description("대분류"),
                            fieldWithPath("result[].orders[].subCategory").description("소분류"),
                            fieldWithPath("result[].coupon").description("사용된 쿠폰 정보 (없으면 null)").optional(),
                            fieldWithPath("result[].coupon.couponId").description("사용된 쿠폰 Id").optional(),
                            fieldWithPath("result[].coupon.userCouponId").description("사용자 쿠폰 ID").optional(),
                            fieldWithPath("result[].coupon.title").description("쿠폰 제목").optional(),
                            fieldWithPath("result[].coupon.couponType").description("쿠폰 타입 (Percent, Fixed)")
                                .optional(),
                            fieldWithPath("result[].coupon.value").description("쿠폰 값 (할인율 또는 할인금액)").optional(),
                            fieldWithPath("result[].amount").description("최종 결제 금액"),
                            fieldWithPath("result[].isUsedCoupon").description("쿠폰 사용 여부"),
                            fieldWithPath("result[].status").description("결제 상태 (SUCCESS, FAIL)"),
                            fieldWithPath("result[].createdAt").description("구매일시")
                        )
                    )
                )
            )
    }
}
