package com.eager.questioncloud.subscribe.service

import com.eager.questioncloud.common.event.EventPublisher
import com.eager.questioncloud.common.event.SubscribeEvent
import com.eager.questioncloud.common.event.SubscribeEventType
import com.eager.questioncloud.common.pagination.PagingInformation
import com.eager.questioncloud.subscribe.dto.SubscribedCreatorInformation
import com.eager.questioncloud.subscribe.implement.SubscribeProcessor
import com.eager.questioncloud.subscribe.implement.SubscribedCreatorInformationReader
import com.eager.questioncloud.subscribe.repository.SubscribeRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class SubscribeService(
    private val subscribeProcessor: SubscribeProcessor,
    private val subscribeRepository: SubscribeRepository,
    private val subscribedCreatorInformationReader: SubscribedCreatorInformationReader,
    private val eventPublisher: EventPublisher
) {
    @Transactional
    fun subscribe(userId: Long, creatorId: Long) {
        subscribeProcessor.subscribe(userId, creatorId)
        eventPublisher.publish(SubscribeEvent(creatorId, SubscribeEventType.SUBSCRIBE))
    }
    
    @Transactional
    fun unSubscribe(userId: Long, creatorId: Long) {
        subscribeProcessor.unSubscribe(userId, creatorId)
        eventPublisher.publish(SubscribeEvent(creatorId, SubscribeEventType.UNSUBSCRIBE))
    }
    
    fun isSubscribed(userId: Long, creatorId: Long): Boolean {
        return subscribeRepository.isSubscribed(userId, creatorId)
    }
    
    fun countCreatorSubscriber(creatorId: Long): Int {
        return subscribeRepository.countSubscriber(creatorId)
    }
    
    fun getMySubscribes(userId: Long, pagingInformation: PagingInformation): List<SubscribedCreatorInformation> {
        return subscribedCreatorInformationReader.getSubscribedCreatorInformation(userId, pagingInformation)
    }
    
    fun countMySubscribe(userId: Long): Int {
        return subscribeRepository.countMySubscribe(userId)
    }
}
