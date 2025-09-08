package com.eager.questioncloud.subscribe.implement

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
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
class SubscribedCreatorInformationReaderTest(
    @Autowired val subscribedCreatorInformationReader: SubscribedCreatorInformationReader,
    @Autowired val subscribeRepository: SubscribeRepository,
    @Autowired val dbCleaner: DBCleaner,
) {
    @MockBean
    lateinit var userQueryAPI: UserQueryAPI
    
    @MockBean
    lateinit var creatorQueryAPI: CreatorQueryAPI
    
    private val userId = 1L
    private val creatorId1 = 101L
    private val creatorId2 = 102L
    private val creatorUserId1 = 201L
    private val creatorUserId2 = 202L
    
    @BeforeEach
    fun setUp() {
        subscribeRepository.save(Subscribe.create(userId, creatorId1))
        subscribeRepository.save(Subscribe.create(userId, creatorId2))
        subscribeRepository.save(Subscribe.create(999L, creatorId1))
    }
    
    @AfterEach
    fun tearDown() {
        dbCleaner.cleanUp()
    }
    
    @Test
    fun `구독한 크리에이터 정보를 조회할 수 있다`() {
        //given
        val pagingInformation = PagingInformation(0, 10)
        
        val creatorQueryData1 = CreatorQueryData(creatorUserId1, creatorId1, "수학", 4.5, 100, 50)
        val creatorQueryData2 = CreatorQueryData(creatorUserId2, creatorId2, "영어", 4.8, 80, 30)
        
        val userQueryData1 = UserQueryData(creatorUserId1, "수학선생님", "math_profile.jpg", "math@test.com")
        val userQueryData2 = UserQueryData(creatorUserId2, "영어선생님", "english_profile.jpg", "english@test.com")
        
        given(creatorQueryAPI.getCreators(listOf(creatorId1, creatorId2)))
            .willReturn(listOf(creatorQueryData1, creatorQueryData2))
        
        given(userQueryAPI.getUsers(listOf(creatorUserId1, creatorUserId2)))
            .willReturn(listOf(userQueryData1, userQueryData2))
        
        //when
        val result = subscribedCreatorInformationReader.getSubscribedCreatorInformation(userId, pagingInformation)
        
        //then
        Assertions.assertThat(result).hasSize(2)
        
        val creator1Info = result.find { it.creatorId == creatorId1 }
        Assertions.assertThat(creator1Info).isNotNull
        Assertions.assertThat(creator1Info!!.creatorName).isEqualTo("수학선생님")
        Assertions.assertThat(creator1Info.profileImage).isEqualTo("math_profile.jpg")
        Assertions.assertThat(creator1Info.mainSubject).isEqualTo("수학")
        Assertions.assertThat(creator1Info.subscriberCount).isEqualTo(2)
        
        val creator2Info = result.find { it.creatorId == creatorId2 }
        Assertions.assertThat(creator2Info).isNotNull
        Assertions.assertThat(creator2Info!!.creatorName).isEqualTo("영어선생님")
        Assertions.assertThat(creator2Info.profileImage).isEqualTo("english_profile.jpg")
        Assertions.assertThat(creator2Info.mainSubject).isEqualTo("영어")
        Assertions.assertThat(creator2Info.subscriberCount).isEqualTo(1)
    }
    
    @Test
    fun `구독한 크리에이터가 없으면 빈 리스트를 반환한다`() {
        //given
        val emptyUserId = 10000L
        val pagingInformation = PagingInformation(0, 10)
        
        given(creatorQueryAPI.getCreators(emptyList())).willReturn(emptyList())
        given(userQueryAPI.getUsers(emptyList())).willReturn(emptyList())
        
        //when
        val result = subscribedCreatorInformationReader.getSubscribedCreatorInformation(emptyUserId, pagingInformation)
        
        //then
        Assertions.assertThat(result).isEmpty()
    }
    
    @Test
    fun `페이징이 적용된다`() {
        //given
        val pagingInformation = PagingInformation(0, 1)
        
        val creatorQueryData1 = CreatorQueryData(creatorUserId1, creatorId1, "수학", 4.5, 100, 50)
        val userQueryData1 = UserQueryData(creatorUserId1, "수학선생님", "math_profile.jpg", "math@test.com")
        
        given(creatorQueryAPI.getCreators(listOf(creatorId1)))
            .willReturn(listOf(creatorQueryData1))
        
        given(userQueryAPI.getUsers(listOf(creatorUserId1)))
            .willReturn(listOf(userQueryData1))
        
        //when
        val result = subscribedCreatorInformationReader.getSubscribedCreatorInformation(userId, pagingInformation)
        
        //then
        Assertions.assertThat(result).hasSize(1)
        Assertions.assertThat(result[0].creatorId).isEqualTo(creatorId1)
    }
}
