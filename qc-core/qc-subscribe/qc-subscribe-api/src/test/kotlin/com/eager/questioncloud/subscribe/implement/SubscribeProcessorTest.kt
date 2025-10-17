package com.eager.questioncloud.subscribe.implement

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.subscribe.domain.Subscribe
import com.eager.questioncloud.subscribe.repository.SubscribeRepository
import com.eager.questioncloud.utils.DBCleaner
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.extensions.ApplyExtension
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
@ApplyExtension(SpringExtension::class)
class SubscribeProcessorTest(
    private val subscribeProcessor: SubscribeProcessor,
    private val subscribeRepository: SubscribeRepository,
    private val dbCleaner: DBCleaner,
) : BehaviorSpec() {
    init {
        afterEach {
            dbCleaner.cleanUp()
        }
        
        Given("구독 하고자 하는 크리에이터가 주어졌을 때") {
            val creatorId = 1L
            val userId = 1L
            
            When("구독하면") {
                subscribeProcessor.subscribe(userId, creatorId)
                
                Then("구독이 완료된다") {
                    val isSubscribed = subscribeRepository.isSubscribed(userId, creatorId)
                    isSubscribed shouldBe true
                }
            }
        }
        
        Given("구독 취소 하고자 하는 크리에이터가 주어졌을 때") {
            val creatorId = 1L
            val userId = 1L
            
            subscribeRepository.save(Subscribe.create(userId, creatorId))
            
            When("구독 취소하면") {
                subscribeProcessor.unSubscribe(userId, creatorId)
                
                Then("구독이 취소된다") {
                    val isSubscribed = subscribeRepository.isSubscribed(userId, creatorId)
                    isSubscribed shouldBe false
                }
            }
        }
        
        Given("구독중이지 않은 크리에이터를") {
            val creatorId = 1L
            val userId = 1L
            
            When("구독 취소하면") {
                Then("예외가 발생한다.") {
                    shouldThrow<CoreException> {
                        subscribeProcessor.unSubscribe(userId, creatorId)
                    }
                }
            }
        }
    }
}
