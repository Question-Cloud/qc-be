package com.eager.questioncloud.payment.question.controller

import com.eager.questioncloud.common.event.DiscountInformation
import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import com.eager.questioncloud.payment.domain.QuestionPayment
import com.eager.questioncloud.payment.domain.QuestionPaymentHistory
import com.eager.questioncloud.payment.domain.QuestionPaymentHistoryOrder
import com.eager.questioncloud.payment.enums.QuestionPaymentStatus
import com.eager.questioncloud.payment.question.dto.QuestionPaymentRequest
import com.eager.questioncloud.payment.question.service.QuestionPaymentHistoryService
import com.eager.questioncloud.payment.question.service.QuestionPaymentService
import com.eager.questioncloud.payment.scenario.QuestionPaymentScenario
import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document
import com.epages.restdocs.apispec.ResourceSnippetParametersBuilder
import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.kotest.core.extensions.ApplyExtension
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
import io.mockk.every
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
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
@ApplyExtension(SpringExtension::class)
class QuestionPaymentControllerDocument(
    @Autowired private val mockMvc: MockMvc,
    @Autowired private val objectMapper: ObjectMapper
) : BehaviorSpec() {
    @MockkBean
    private lateinit var questionPaymentHistoryService: QuestionPaymentHistoryService
    
    @MockkBean
    private lateinit var questionPaymentService: QuestionPaymentService
    
    init {
        Given("문제 구매 API 테스트") {
            val questionPaymentRequest = QuestionPaymentRequest(
                questionIds = listOf(1L, 2L),
                userCouponId = 1L
            )
            val userId = 1L
            val questionPaymentScenario = QuestionPaymentScenario.create(questionPaymentRequest.questionIds.size)
            every { questionPaymentService.payment(any()) } returns QuestionPayment.create(
                userId,
                questionPaymentScenario.order
            )
            
            When("유효한 문제들과 쿠폰으로 구매 요청을 하면") {
                Then("구매가 성공한다") {
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
            }
        }
        
        Given("이미 구매한 문제로 구매를 시도할 때") {
            val questionPaymentRequest = QuestionPaymentRequest(
                questionIds = listOf(101L),
                userCouponId = null
            )
            every { questionPaymentService.payment(any()) } throws
                    CoreException(Error.ALREADY_OWN_QUESTION)
            When("중복 구매 요청을 하면") {
                Then("ALREADY_OWN_QUESTION 예외가 발생한다") {
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
            }
        }
        
        Given("사용할 수 없는 문제로 구매를 시도할 때") {
            val questionPaymentRequest = QuestionPaymentRequest(
                questionIds = listOf(999L, 1000L),
                userCouponId = null
            )
            every { questionPaymentService.payment(any()) } throws CoreException(Error.UNAVAILABLE_QUESTION)
            
            When("비활성화된 문제로 구매 요청을 하면") {
                Then("UNAVAILABLE_QUESTION 예외가 발생한다") {
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
            }
        }
        
        Given("포인트가 부족한 상황에서 구매를 시도할 때") {
            val questionPaymentRequest = QuestionPaymentRequest(
                questionIds = listOf(101L, 102L),
                userCouponId = null
            )
            every { questionPaymentService.payment(any()) } throws CoreException(Error.NOT_ENOUGH_POINT)
            
            When("포인트 부족 상태로 구매 요청을 하면") {
                Then("NOT_ENOUGH_POINT 예외가 발생한다") {
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
            }
        }
        
        Given("문제 구매 내역을 조회할 때") {
            val sampleOrders = listOf(
                QuestionPaymentHistoryOrder(
                    questionId = 101L,
                    realPrice = 5000,
                    title = "미적분 기본 문제집",
                    thumbnail = "https://example.com/thumbnail1.jpg",
                    creatorName = "김수학",
                    subject = "Mathematics",
                    mainCategory = "미적분",
                    subCategory = "극한"
                ),
                QuestionPaymentHistoryOrder(
                    questionId = 102L,
                    realPrice = 3000,
                    title = "물리 역학 문제집",
                    thumbnail = "https://example.com/thumbnail2.jpg",
                    creatorName = "이물리",
                    subject = "Physics",
                    mainCategory = "역학",
                    subCategory = "운동"
                )
            )
            
            val sampleQuestionPaymentHistories = listOf(
                QuestionPaymentHistory(
                    orderId = "order-123456",
                    userId = 1L,
                    orders = sampleOrders,
                    discountInformation = listOf(DiscountInformation("coupon", 1000)),
                    realAmount = 7200,
                    status = QuestionPaymentStatus.SUCCESS,
                    createdAt = LocalDateTime.of(2024, 3, 15, 14, 30)
                ),
                QuestionPaymentHistory(
                    orderId = "order-789012",
                    userId = 1L,
                    orders = listOf(sampleOrders[0]),
                    discountInformation = listOf(DiscountInformation("coupon", 3000)),
                    realAmount = 5000,
                    status = QuestionPaymentStatus.SUCCESS,
                    createdAt = LocalDateTime.of(2024, 3, 10, 9, 15)
                )
            )
            val totalCount = 25
            
            every { questionPaymentHistoryService.countQuestionPaymentHistory(any()) } returns totalCount
            every { questionPaymentHistoryService.getQuestionPaymentHistory(any(), any()) } returns sampleQuestionPaymentHistories
            
            When("페이징 조건으로 내역 조회를 요청하면") {
                Then("구매 내역 목록이 페이징되어 반환된다") {
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
                                        fieldWithPath("result[].discountInformation[]").description("할인 정보"),
                                        fieldWithPath("result[].discountInformation[].title").description("할인 종류"),
                                        fieldWithPath("result[].discountInformation[].value").description("할인금액"),
                                        fieldWithPath("result[].amount").description("최종 결제 금액"),
                                        fieldWithPath("result[].status").description("결제 상태 (SUCCESS, FAIL)"),
                                        fieldWithPath("result[].createdAt").description("구매일시")
                                    )
                                )
                            )
                        )
                }
            }
        }
    }
}
