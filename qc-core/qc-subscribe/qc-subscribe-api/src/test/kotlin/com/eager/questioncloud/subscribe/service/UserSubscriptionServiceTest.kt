package com.eager.questioncloud.subscribe.service

import com.eager.questioncloud.common.pagination.PagingInformation
import com.eager.questioncloud.subscribe.dto.SubscribedCreatorInformation
import com.eager.questioncloud.subscribe.implement.SubscribedCreatorInformationReader
import com.eager.questioncloud.subscribe.repository.SubscribeRepository
import com.eager.questioncloud.subscribe.scenario.SubscribeScenario
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

class UserSubscriptionServiceTest : BehaviorSpec() {
    private val subscribedCreatorInformationReader = mockk<SubscribedCreatorInformationReader>()
    private val subscribeRepository = mockk<SubscribeRepository>()
    private val userSubscriptionService = UserSubscriptionService(subscribedCreatorInformationReader, subscribeRepository)
    
    init {
        Given("구독 여부 확인 요청이 주어졌을 때") {
            val userId = 1L
            val creatorId = 1L
            
            every { subscribeRepository.isSubscribed(userId, creatorId) } returns true
            
            When("구독 여부를 확인하면") {
                val isSubscribed = userSubscriptionService.isSubscribed(userId, creatorId)
                
                Then("구독 중임을 반환한다") {
                    isSubscribed shouldBe true
                    verify(exactly = 1) { subscribeRepository.isSubscribed(userId, creatorId) }
                }
            }
        }
        
        Given("사용자가 구독 리스트 조회 요청을 했을 때") {
            val userId = 1L
            
            val pagingInformation = PagingInformation(0, 10)
            val scenario = SubscribeScenario.create(userId, 5)
            val creatorQueryDatas = scenario.creatorQueryDatas
            val userQueryDatas = scenario.userQueryDatas
            
            val subscribedCreatorInformations = creatorQueryDatas.mapIndexed { index, creatorQueryData ->
                val userQueryData = userQueryDatas[index]
                SubscribedCreatorInformation(
                    creatorId = creatorQueryData.creatorId,
                    creatorName = userQueryData.name,
                    profileImage = userQueryData.profileImage,
                    subscriberCount = creatorQueryData.subscriberCount,
                    mainSubject = creatorQueryData.mainSubject
                )
            }
            
            every {
                subscribedCreatorInformationReader.getSubscribedCreatorInformation(
                    userId,
                    pagingInformation
                )
            } returns subscribedCreatorInformations
            
            When("구독 리스트를 조회하면") {
                val subscribedCreators = userSubscriptionService.getMySubscribes(userId, pagingInformation)
                
                Then("구독 리스트가 반환된다") {
                    subscribedCreators.size shouldBe 5
                    verify(exactly = 1) { subscribedCreatorInformationReader.getSubscribedCreatorInformation(userId, pagingInformation) }
                }
            }
        }
        
        Given("내 구독 수 조회 요청이 주어졌을 때") {
            val userId = 1L
            
            every { subscribeRepository.countMySubscribe(userId) } returns 3
            
            When("내 구독 수를 조회하면") {
                val mySubscribeCount = userSubscriptionService.countMySubscribe(userId)
                
                Then("내 구독 수를 반환한다") {
                    mySubscribeCount shouldBe 3
                    verify(exactly = 1) { subscribeRepository.countMySubscribe(userId) }
                }
            }
        }
    }
}
