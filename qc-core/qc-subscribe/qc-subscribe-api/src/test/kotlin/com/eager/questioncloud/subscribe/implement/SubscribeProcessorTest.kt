package com.eager.questioncloud.subscribe.implement

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import com.eager.questioncloud.creator.api.internal.CreatorQueryAPI
import com.eager.questioncloud.subscribe.domain.Subscribe
import com.eager.questioncloud.subscribe.repository.SubscribeRepository
import com.eager.questioncloud.utils.DBCleaner
import com.ninjasquad.springmockk.MockkBean
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.extensions.ApplyExtension
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import io.mockk.every
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
    @MockkBean
    lateinit var creatorQueryAPI: CreatorQueryAPI
    
    init {
        afterEach {
            dbCleaner.cleanUp()
        }
        
        Given("존재하는 크리에이터가 주어졌을 때") {
            val creatorId = 1L
            val userId = 1L
            every { creatorQueryAPI.isExistsById(creatorId) } returns true
            
            When("구독하면") {
                subscribeProcessor.subscribe(userId, creatorId)
                
                Then("구독이 완료된다") {
                    val isSubscribed = subscribeRepository.isSubscribed(userId, creatorId)
                    isSubscribed shouldBe true
                }
            }
        }
        
        Given("존재하지 않는 크리에이터가 주어졌을 때") {
            val creatorId = 1L
            val userId = 1L
            
            every { creatorQueryAPI.isExistsById(creatorId) } returns false
            
            When("구독하면") {
                Then("NOT_FOUND 예외가 발생한다") {
                    val exception = shouldThrow<CoreException> {
                        subscribeProcessor.subscribe(userId, creatorId)
                    }
                    exception.error shouldBe Error.NOT_FOUND
                }
            }
        }
        
        Given("이미 크리에이터를 구독한 상태에서") {
            val creatorId = 1L
            val userId = 1L
            
            subscribeRepository.save(Subscribe.create(userId, creatorId))
            
            every { creatorQueryAPI.isExistsById(creatorId) } returns true
            
            When("다시 구독하면") {
                Then("ALREADY_SUBSCRIBE_CREATOR 예외가 발생한다") {
                    val exception = shouldThrow<CoreException> {
                        subscribeProcessor.subscribe(userId, creatorId)
                    }
                    exception.error shouldBe Error.ALREADY_SUBSCRIBE_CREATOR
                }
            }
        }
        
        Given("구독 취소 하고자 하는 크리에이터가 주어졌을 때") {
            val creatorId = 1L
            val userId = 1L
            
            every { creatorQueryAPI.isExistsById(creatorId) } returns true
            subscribeRepository.save(Subscribe.create(userId, creatorId))
            When("구독을 취소하면") {
                subscribeProcessor.unSubscribe(userId, creatorId)
                
                Then("구독이 취소된다") {
                    val isSubscribed = subscribeRepository.isSubscribed(userId, creatorId)
                    isSubscribed shouldBe false
                }
            }
        }
    }
}
