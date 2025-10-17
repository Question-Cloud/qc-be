package com.eager.questioncloud.subscribe.service

import com.eager.questioncloud.common.event.EventPublisher
import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import com.eager.questioncloud.subscribe.implement.SubscribeProcessor
import com.eager.questioncloud.subscribe.implement.SubscribeValidator
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.*

class SubscribeServiceTest : BehaviorSpec() {
    private val subscribeValidator = mockk<SubscribeValidator>()
    private val subscribeProcessor = mockk<SubscribeProcessor>()
    private val eventPublisher = mockk<EventPublisher>()
    
    private val subscribeService = SubscribeService(
        subscribeValidator,
        subscribeProcessor,
        eventPublisher
    )
    
    init {
        afterEach {
            clearMocks(subscribeProcessor, subscribeValidator, eventPublisher)
        }
        
        val userId = 1L
        val creatorId = 2L
        
        Given("구독 요청이 주어졌을 때") {
            justRun { subscribeValidator.validate(userId, creatorId) }
            justRun { subscribeProcessor.subscribe(userId, creatorId) }
            justRun { eventPublisher.publish(any()) }
            
            When("구독을 하면") {
                subscribeService.subscribe(userId, creatorId)
                
                Then("구독이 생성되고 이벤트가 발행된다") {
                    verify(exactly = 1) { subscribeProcessor.subscribe(userId, creatorId) }
                    verify(exactly = 1) { eventPublisher.publish(any()) }
                }
            }
        }
        
        Given("유효하지 않은 구독 요청이 주어졌을 때 (존재하지 않는 크리에이터, 이미 구독)") {
            every { subscribeValidator.validate(userId, creatorId) } throws CoreException(Error.ALREADY_SUBSCRIBE_CREATOR)
            
            When("구독을 하면") {
                Then("예외가 발생한다.") {
                    shouldThrow<CoreException> {
                        subscribeService.subscribe(userId, creatorId)
                    }
                }
            }
        }
        
        Given("구독이 되어 있는 상태에서") {
            justRun { subscribeProcessor.unSubscribe(userId, creatorId) }
            justRun { eventPublisher.publish(any()) }
            
            When("구독을 취소하면") {
                subscribeService.unSubscribe(userId, creatorId)
                
                Then("구독이 취소되고 이벤트가 발행된다") {
                    verify(exactly = 1) { subscribeProcessor.unSubscribe(userId, creatorId) }
                    verify(exactly = 1) { eventPublisher.publish(any()) }
                }
            }
        }
    }
}