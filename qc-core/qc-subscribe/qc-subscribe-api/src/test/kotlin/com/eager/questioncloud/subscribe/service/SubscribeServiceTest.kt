package com.eager.questioncloud.subscribe.service

import com.eager.questioncloud.common.event.EventPublisher
import com.eager.questioncloud.common.pagination.PagingInformation
import com.eager.questioncloud.subscribe.dto.SubscribedCreatorInformation
import com.eager.questioncloud.subscribe.implement.SubscribeProcessor
import com.eager.questioncloud.subscribe.implement.SubscribedCreatorInformationReader
import com.eager.questioncloud.subscribe.repository.SubscribeRepository
import com.eager.questioncloud.subscribe.scenario.SubscribeScenario
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.*

class SubscribeServiceTest : BehaviorSpec() {
    private val subscribeProcessor = mockk<SubscribeProcessor>()
    private val subscribeRepository = mockk<SubscribeRepository>()
    private val subscribedCreatorInformationReader = mockk<SubscribedCreatorInformationReader>()
    private val eventPublisher = mockk<EventPublisher>()
    
    private val subscribeService = SubscribeService(
        subscribeProcessor,
        subscribeRepository,
        subscribedCreatorInformationReader,
        eventPublisher
    )
    
    init {
        afterEach {
            clearMocks(subscribeProcessor, subscribeRepository, subscribedCreatorInformationReader, eventPublisher)
        }
        
        val userId = 1L
        val creatorId = 2L
        
        Given("구독 생성 요청이 주어졌을 때") {
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
        
        Given("구독 여부 확인 요청이 주어졌을 때") {
            every { subscribeRepository.isSubscribed(userId, creatorId) } returns true
            
            When("구독 여부를 확인하면") {
                val isSubscribed = subscribeService.isSubscribed(userId, creatorId)
                
                Then("구독 중임을 반환한다") {
                    isSubscribed shouldBe true
                    verify(exactly = 1) { subscribeRepository.isSubscribed(userId, creatorId) }
                }
            }
        }
        
        Given("크리에이터의 구독자 수 조회 요청이 주어졌을 때") {
            every { subscribeRepository.countSubscriber(creatorId) } returns 5
            
            When("구독자 수를 조회하면") {
                val subscriberCount = subscribeService.countCreatorSubscriber(creatorId)
                
                Then("구독자 수를 반환한다") {
                    subscriberCount shouldBe 5
                    verify(exactly = 1) { subscribeRepository.countSubscriber(creatorId) }
                }
            }
        }
        
        Given("사용자가 구독 리스트 조회 요청을 했을 때") {
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
                val subscribedCreators = subscribeService.getMySubscribes(userId, pagingInformation)
                
                Then("구독 리스트가 반환된다") {
                    subscribedCreators.size shouldBe 5
                    verify(exactly = 1) { subscribedCreatorInformationReader.getSubscribedCreatorInformation(userId, pagingInformation) }
                }
            }
        }
        
        Given("내 구독 수 조회 요청이 주어졌을 때") {
            every { subscribeRepository.countMySubscribe(userId) } returns 3
            
            When("내 구독 수를 조회하면") {
                val mySubscribeCount = subscribeService.countMySubscribe(userId)
                
                Then("내 구독 수를 반환한다") {
                    mySubscribeCount shouldBe 3
                    verify(exactly = 1) { subscribeRepository.countMySubscribe(userId) }
                }
            }
        }
    }
}