package com.eager.questioncloud.subscribe.service

import com.eager.questioncloud.common.pagination.PagingInformation
import com.eager.questioncloud.creator.api.internal.CreatorQueryAPI
import com.eager.questioncloud.creator.api.internal.CreatorQueryData
import com.eager.questioncloud.subscribe.domain.Subscribe
import com.eager.questioncloud.subscribe.repository.SubscribeRepository
import com.eager.questioncloud.user.api.internal.UserQueryAPI
import com.eager.questioncloud.user.api.internal.UserQueryData
import com.eager.questioncloud.utils.DBCleaner
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
class SubscribeServiceTest(
    @Autowired val subscribeService: SubscribeService,
    @Autowired val subscribeRepository: SubscribeRepository,
    @Autowired val dbCleaner: DBCleaner,
) {
    @MockBean
    lateinit var userQueryAPI: UserQueryAPI
    
    @MockBean
    lateinit var creatorQueryAPI: CreatorQueryAPI
    
    private val userId = 1L
    private val creatorId = 2L
    private val creatorUserId = 100L
    
    @AfterEach
    fun tearDown() {
        dbCleaner.cleanUp()
    }
    
    @Test
    fun `구독을 할 수 있다`() {
        //given
        given(creatorQueryAPI.isExistsById(creatorId)).willReturn(true)
        
        //when
        subscribeService.subscribe(userId, creatorId)
        
        //then
        val isSubscribed = subscribeService.isSubscribed(userId, creatorId)
        Assertions.assertThat(isSubscribed).isTrue()
    }
    
    @Test
    fun `구독을 취소할 수 있다`() {
        //given
        given(creatorQueryAPI.isExistsById(creatorId)).willReturn(true)
        subscribeService.subscribe(userId, creatorId)
        
        //when
        subscribeService.unSubscribe(userId, creatorId)
        
        //then
        val isSubscribed = subscribeService.isSubscribed(userId, creatorId)
        Assertions.assertThat(isSubscribed).isFalse()
    }
    
    @Test
    fun `구독 여부를 확인할 수 있다`() {
        //given
        given(creatorQueryAPI.isExistsById(creatorId)).willReturn(true)
        subscribeService.subscribe(userId, creatorId)
        
        //when
        val isSubscribed = subscribeService.isSubscribed(userId, creatorId)
        
        //then
        Assertions.assertThat(isSubscribed).isTrue()
    }
    
    @Test
    fun `크리에이터의 구독자 수를 조회할 수 있다`() {
        //given
        given(creatorQueryAPI.isExistsById(creatorId)).willReturn(true)
        subscribeService.subscribe(userId, creatorId)
        subscribeRepository.save(Subscribe.create(999L, creatorId))
        
        //when
        val subscriberCount = subscribeService.countCreatorSubscriber(creatorId)
        
        //then
        Assertions.assertThat(subscriberCount).isEqualTo(2)
    }
    
    @Test
    fun `내 구독 리스트를 조회할 수 있다`() {
        //given
        given(creatorQueryAPI.isExistsById(creatorId)).willReturn(true)
        subscribeService.subscribe(userId, creatorId)
        
        val pagingInformation = PagingInformation(0, 10)
        val creatorQueryData = CreatorQueryData(creatorUserId, creatorId, "수학", 4.5, 100, 50)
        val userQueryData = UserQueryData(creatorUserId, "수학선생님", "profile.jpg", "creator@test.com")
        
        given(creatorQueryAPI.getCreators(listOf(creatorId))).willReturn(listOf(creatorQueryData))
        given(userQueryAPI.getUsers(listOf(creatorUserId))).willReturn(listOf(userQueryData))
        
        //when
        val subscribedCreators = subscribeService.getMySubscribes(userId, pagingInformation)
        
        //then
        Assertions.assertThat(subscribedCreators).hasSize(1)
        Assertions.assertThat(subscribedCreators[0].creatorId).isEqualTo(creatorId)
        Assertions.assertThat(subscribedCreators[0].creatorName).isEqualTo("수학선생님")
    }
    
    @Test
    fun `내 구독 수를 조회할 수 있다`() {
        //given
        given(creatorQueryAPI.isExistsById(creatorId)).willReturn(true)
        subscribeService.subscribe(userId, creatorId)
        
        //when
        val mySubscribeCount = subscribeService.countMySubscribe(userId)
        
        //then
        Assertions.assertThat(mySubscribeCount).isEqualTo(1)
    }
}
