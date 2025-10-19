package com.eager.questioncloud.subscribe.implement

import com.eager.questioncloud.common.pagination.PagingInformation
import com.eager.questioncloud.creator.api.internal.CreatorQueryAPI
import com.eager.questioncloud.subscribe.repository.SubscribeRepository
import com.eager.questioncloud.subscribe.scenario.SubscribeScenario
import com.eager.questioncloud.user.api.internal.UserQueryAPI
import com.eager.questioncloud.utils.DBCleaner
import com.ninjasquad.springmockk.MockkBean
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
class UserSubscriptionReaderTest(
    private val userSubscriptionReader: UserSubscriptionReader,
    private val subscribeRepository: SubscribeRepository,
    private val dbCleaner: DBCleaner,
) : BehaviorSpec() {
    @MockkBean
    lateinit var userQueryAPI: UserQueryAPI
    
    @MockkBean
    lateinit var creatorQueryAPI: CreatorQueryAPI
    
    init {
        afterEach {
            dbCleaner.cleanUp()
        }
        
        Given("구독 중인 크리에이터 목록 조회") {
            val pagingInformation = PagingInformation.max
            val userId = 1L
            val subscribedScenario = SubscribeScenario.create(userId)
            
            subscribedScenario.subscribeds.forEach {
                subscribeRepository.save(it)
            }
            
            every { creatorQueryAPI.getCreators(any()) } returns subscribedScenario.creatorQueryDatas
            every { userQueryAPI.getUsers(any()) } returns subscribedScenario.userQueryDatas
            
            When("구독한 크리에이터 정보를 조회하면") {
                val result = userSubscriptionReader.getUserSubscriptionDetails(userId, pagingInformation)
                
                Then("구독한 크리에이터 정보가 반환된다") {
                    result.size shouldBe subscribedScenario.subscribeds.size
                    
                    result.forEach { subscribe ->
                        val creatorQuery = subscribedScenario.creatorQueryDatas.find { subscribe.creatorId == it.creatorId }!!
                        val userQuery = subscribedScenario.userQueryDatas.find { creatorQuery.userId == it.userId }!!
                        
                        subscribe.creatorName shouldBe userQuery.name
                        subscribe.profileImage shouldBe userQuery.profileImage
                        subscribe.mainSubject shouldBe creatorQuery.mainSubject
                        subscribe.subscriberCount shouldBe creatorQuery.subscriberCount
                    }
                }
            }
        }
        
        Given("구독 중인 크리에이터 수 조회") {
            val userId = 1L
            val subscribedScenario = SubscribeScenario.create(userId)
            
            subscribedScenario.subscribeds.forEach {
                subscribeRepository.save(it)
            }
            
            When("구독한 크리에이터 수를 조회하면") {
                val result = userSubscriptionReader.countMySubscribe(userId)
                Then("구독한 크리에이터 수가 반환된다.") {
                    result shouldBe subscribedScenario.subscribeds.size
                }
            }
        }
        
        Given("구독 여부 조회") {
            val userId = 1L
            val subscribedScenario = SubscribeScenario.create(userId, count = 1)
            
            subscribedScenario.subscribeds.forEach {
                subscribeRepository.save(it)
            }
            
            val subscribedCreator = subscribedScenario.subscribeds[0].creatorId
            val nonSubscribedCreator = 100L
            
            When("크리에이터의 구독 여부를 조회하면") {
                val result1 = userSubscriptionReader.isSubscribed(userId, subscribedCreator)
                val result2 = userSubscriptionReader.isSubscribed(userId, nonSubscribedCreator)
                
                Then("구독 여부가 반환된다.") {
                    result1 shouldBe true
                    result2 shouldBe false
                }
            }
        }
    }
}
