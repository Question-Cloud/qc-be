package com.eager.questioncloud.subscribe.implement

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import com.eager.questioncloud.creator.api.internal.CreatorQueryAPI
import com.eager.questioncloud.subscribe.domain.Subscribe
import com.eager.questioncloud.subscribe.repository.SubscribeRepository
import com.eager.questioncloud.test.utils.DBCleaner
import com.ninjasquad.springmockk.MockkBean
import io.kotest.assertions.throwables.shouldNotThrowAny
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
class SubscribeValidatorTest(
    private val subscribeValidator: SubscribeValidator,
    private val subscribeRepository: SubscribeRepository,
    private val dbCleaner: DBCleaner,
) : BehaviorSpec() {
    @MockkBean
    private lateinit var creatorQueryAPI: CreatorQueryAPI
    
    init {
        afterTest {
            dbCleaner.cleanUp()
        }
        
        Given("구독 가능한 크리에이터가 있을 때") {
            val userId = 100L
            val creatorId = 200L
            
            every { creatorQueryAPI.isExistsById(creatorId) } returns true
            
            When("구독 가능 여부를 검증하면") {
                Then("예외가 발생하지 않는다") {
                    shouldNotThrowAny {
                        subscribeValidator.validate(userId, creatorId)
                    }
                }
            }
        }
        
        Given("존재하지 않는 크리에이터일 때") {
            val userId = 101L
            val creatorId = 201L
            
            every { creatorQueryAPI.isExistsById(creatorId) } returns false
            
            When("구독 가능 여부를 검증하면") {
                Then("CoreException(Error.NOT_FOUND)이 발생한다") {
                    val exception = shouldThrow<CoreException> {
                        subscribeValidator.validate(userId, creatorId)
                    }
                    exception.error shouldBe Error.NOT_FOUND
                }
            }
        }
        
        Given("이미 구독한 크리에이터일 때") {
            val userId = 102L
            val creatorId = 202L
            
            subscribeRepository.save(Subscribe.create(userId, creatorId))
            every { creatorQueryAPI.isExistsById(creatorId) } returns true
            
            When("구독 가능 여부를 검증하면") {
                Then("CoreException(Error.ALREADY_SUBSCRIBE_CREATOR)이 발생한다") {
                    val exception = shouldThrow<CoreException> {
                        subscribeValidator.validate(userId, creatorId)
                    }
                    exception.error shouldBe Error.ALREADY_SUBSCRIBE_CREATOR
                }
            }
        }
    }
}
