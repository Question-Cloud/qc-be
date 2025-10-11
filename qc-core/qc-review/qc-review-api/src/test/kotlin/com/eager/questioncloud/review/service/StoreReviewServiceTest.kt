package com.eager.questioncloud.review.service

import com.eager.questioncloud.common.event.EventPublisher
import com.eager.questioncloud.common.pagination.PagingInformation
import com.eager.questioncloud.review.ReviewScenario
import com.eager.questioncloud.review.command.DeleteReviewCommand
import com.eager.questioncloud.review.command.ModifyReviewCommand
import com.eager.questioncloud.review.command.RegisterReviewCommand
import com.eager.questioncloud.review.domain.QuestionReview
import com.eager.questioncloud.review.dto.MyQuestionReview
import com.eager.questioncloud.review.dto.QuestionReviewDetail
import com.eager.questioncloud.review.dto.ReviewerStatistics
import com.eager.questioncloud.review.implement.StoreReviewReader
import com.eager.questioncloud.review.implement.StoreReviewRegister
import com.eager.questioncloud.review.implement.StoreReviewRemover
import com.eager.questioncloud.review.implement.StoreReviewUpdater
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.*
import java.time.LocalDateTime

class StoreReviewServiceTest : BehaviorSpec() {
    private val storeReviewReader = mockk<StoreReviewReader>()
    private val storeReviewRegister = mockk<StoreReviewRegister>()
    private val storeReviewUpdater = mockk<StoreReviewUpdater>()
    private val storeReviewRemover = mockk<StoreReviewRemover>()
    private val eventPublisher = mockk<EventPublisher>()
    
    private val storeReviewService = StoreReviewService(
        storeReviewReader,
        storeReviewRegister,
        storeReviewUpdater,
        storeReviewRemover,
        eventPublisher
    )
    
    init {
        afterEach {
            clearMocks(
                storeReviewReader,
                storeReviewRegister,
                storeReviewUpdater,
                storeReviewRemover,
                eventPublisher
            )
        }
        
        Given("리뷰 개수 조회") {
            val questionId = 1L
            every { storeReviewReader.count(questionId) } returns 3
            
            When("리뷰 개수를 조회하면") {
                val count = storeReviewService.count(questionId)
                Then("리뷰 개수가 반환된다") {
                    count shouldBe 3
                    verify(exactly = 1) { storeReviewReader.count(questionId) }
                }
            }
        }
        
        Given("리뷰 상세 정보 조회") {
            val questionId = 1L
            val userId = 1L
            val pagingInformation = PagingInformation(0, 10)
            val reviewScenario = ReviewScenario.create(questionId, 3)
            
            val reviewDetails = reviewScenario.reviews.mapIndexed { index, review ->
                QuestionReviewDetail(
                    id = review.id,
                    reviewerName = reviewScenario.userQueryDatas[index].name,
                    reviewerStatistics = ReviewerStatistics(10, 4.5),
                    rate = review.rate,
                    comment = review.comment,
                    isWriter = review.reviewerId == userId,
                    createdAt = LocalDateTime.now()
                )
            }
            
            every {
                storeReviewReader.getQuestionReviewDetails(questionId, userId, pagingInformation)
            } returns reviewDetails
            
            When("리뷰 상세 정보를 조회하면") {
                val result = storeReviewService.getReviewDetails(questionId, userId, pagingInformation)
                Then("리뷰 상세 정보가 반환된다") {
                    result shouldBe reviewDetails
                    verify(exactly = 1) {
                        storeReviewReader.getQuestionReviewDetails(questionId, userId, pagingInformation)
                    }
                }
            }
        }
        
        Given("내 리뷰 조회") {
            val questionId = 1L
            val userId = 1L
            val reviewScenario = ReviewScenario.createMyReveiw(questionId, userId)
            val review = reviewScenario.reviews[0]
            val myReview = MyQuestionReview(
                id = review.id,
                rate = review.rate,
                comment = review.comment
            )
            
            every { storeReviewReader.getMyQuestionReview(questionId, userId) } returns myReview
            
            When("내 리뷰를 조회하면") {
                val result = storeReviewService.getMyReview(questionId, userId)
                Then("내 리뷰 정보가 반환된다") {
                    result.comment shouldBe review.comment
                    result.rate shouldBe review.rate
                    verify(exactly = 1) { storeReviewReader.getMyQuestionReview(questionId, userId) }
                }
            }
        }
        
        Given("리뷰 등록") {
            val questionId = 1L
            val userId = 1L
            val registerReviewCommand = RegisterReviewCommand(questionId, userId, "새로운 리뷰", 4)
            val review = QuestionReview.create(
                registerReviewCommand.questionId,
                registerReviewCommand.reviewerId,
                registerReviewCommand.comment,
                registerReviewCommand.rate
            )
            
            every { storeReviewRegister.register(registerReviewCommand) } returns review
            justRun { eventPublisher.publish(any()) }
            
            When("리뷰를 등록하면") {
                storeReviewService.register(registerReviewCommand)
                Then("리뷰가 등록되고 이벤트가 발행된다") {
                    verify(exactly = 1) { storeReviewRegister.register(registerReviewCommand) }
                    verify(exactly = 1) { eventPublisher.publish(any()) }
                }
            }
        }
        
        Given("리뷰 수정") {
            val reviewId = 100L
            val questionId = 1L
            val userId = 1L
            val newComment = "수정된 리뷰"
            val newRate = 5
            val modifyReviewCommand = ModifyReviewCommand(reviewId, userId, newComment, newRate)
            every { storeReviewUpdater.modify(modifyReviewCommand) } returns Pair(questionId, 2)
            justRun { eventPublisher.publish(any()) }
            
            When("리뷰를 수정하면") {
                storeReviewService.modify(modifyReviewCommand)
                Then("리뷰가 수정되고 이벤트가 발행된다") {
                    verify(exactly = 1) { storeReviewUpdater.modify(modifyReviewCommand) }
                    verify(exactly = 1) { eventPublisher.publish(any()) }
                }
            }
        }
        
        Given("리뷰 삭제") {
            val reviewId = 100L
            val questionId = 1L
            val userId = 1L
            
            val deleteReviewCommand = DeleteReviewCommand(reviewId, userId)
            
            every { storeReviewRemover.delete(deleteReviewCommand) } returns Pair(questionId, 5)
            justRun { eventPublisher.publish(any()) }
            
            When("리뷰를 삭제하면") {
                storeReviewService.delete(deleteReviewCommand)
                Then("리뷰가 삭제되고 이벤트가 발행된다") {
                    verify(exactly = 1) { storeReviewRemover.delete(deleteReviewCommand) }
                    verify(exactly = 1) { eventPublisher.publish(any()) }
                }
            }
        }
        
        Given("여러 리뷰 작업 연속 수행") {
            val reviewId = 100L
            val questionId1 = 1L
            val questionId2 = 2L
            val questionId3 = 3L
            val userId = 1L
            
            val registerReviewCommand1 = RegisterReviewCommand(questionId2, userId, "리뷰2", 4)
            val registerReviewCommand2 = RegisterReviewCommand(questionId3, userId, "리뷰3", 2)
            val modifyReviewCommand1 = ModifyReviewCommand(reviewId, userId, "수정된 리뷰", 1)
            
            every { storeReviewRegister.register(registerReviewCommand1) } returns QuestionReview.create(
                registerReviewCommand1.questionId,
                registerReviewCommand1.reviewerId,
                registerReviewCommand1.comment,
                registerReviewCommand1.rate
            )
            
            every { storeReviewRegister.register(registerReviewCommand2) } returns QuestionReview.create(
                registerReviewCommand2.questionId,
                registerReviewCommand2.reviewerId,
                registerReviewCommand2.comment,
                registerReviewCommand2.rate
            )
            
            every { storeReviewUpdater.modify(modifyReviewCommand1) } returns Pair(questionId1, -2)
            
            justRun { eventPublisher.publish(any()) }
            
            every { storeReviewReader.count(questionId1) } returns 1
            every { storeReviewReader.count(questionId2) } returns 1
            every { storeReviewReader.count(questionId3) } returns 1
            
            every { storeReviewReader.getMyQuestionReview(questionId1, userId) } returns MyQuestionReview(
                1L,
                1,
                "수정된 리뷰"
            )
            every { storeReviewReader.getMyQuestionReview(questionId2, userId) } returns MyQuestionReview(
                2L,
                4,
                "리뷰2"
            )
            every { storeReviewReader.getMyQuestionReview(questionId3, userId) } returns MyQuestionReview(
                3L,
                2,
                "리뷰3"
            )
            
            When("여러 리뷰 작업을 연속으로 수행하면") {
                storeReviewService.register(registerReviewCommand1)
                storeReviewService.register(registerReviewCommand2)
                storeReviewService.modify(modifyReviewCommand1)
                
                Then("모든 작업이 정상적으로 수행된다") {
                    storeReviewService.count(questionId1) shouldBe 1
                    storeReviewService.count(questionId2) shouldBe 1
                    storeReviewService.count(questionId3) shouldBe 1
                    
                    val myReview1 = storeReviewService.getMyReview(questionId1, userId)
                    val myReview2 = storeReviewService.getMyReview(questionId2, userId)
                    val myReview3 = storeReviewService.getMyReview(questionId3, userId)
                    
                    myReview1.comment shouldBe "수정된 리뷰"
                    myReview1.rate shouldBe 1
                    
                    myReview2.comment shouldBe "리뷰2"
                    myReview2.rate shouldBe 4
                    
                    myReview3.comment shouldBe "리뷰3"
                    myReview3.rate shouldBe 2
                    
                    verify(exactly = 2) { storeReviewRegister.register(any()) }
                    verify(exactly = 1) { storeReviewUpdater.modify(modifyReviewCommand1) }
                    verify(exactly = 3) { eventPublisher.publish(any()) }
                }
            }
        }
    }
}