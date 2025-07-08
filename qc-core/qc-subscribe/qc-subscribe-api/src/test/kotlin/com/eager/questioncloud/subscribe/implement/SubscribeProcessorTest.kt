package com.eager.questioncloud.subscribe.implement

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import com.eager.questioncloud.creator.api.internal.CreatorQueryAPI
import com.eager.questioncloud.subscribe.infrastructure.repository.SubscribeRepository
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
class SubscribeProcessorTest(
    @Autowired val subscribeProcessor: SubscribeProcessor,
    @Autowired val subscribeRepository: SubscribeRepository,
    @Autowired val dbCleaner: DBCleaner,
) {
    @MockBean
    lateinit var creatorQueryAPI: CreatorQueryAPI

    private val userId = 1L
    private val creatorId = 2L

    @AfterEach
    fun tearDown() {
        dbCleaner.cleanUp()
    }

    @Test
    fun `구독을 할 수 있다`() {
        //given
        given(creatorQueryAPI.isExistsById(creatorId)).willReturn(true)

        //when
        subscribeProcessor.subscribe(userId, creatorId)

        //then
        val isSubscribed = subscribeRepository.isSubscribed(userId, creatorId)
        Assertions.assertThat(isSubscribed).isTrue()
    }

    @Test
    fun `존재하지 않는 크리에이터를 구독할 수 없다`() {
        //given
        given(creatorQueryAPI.isExistsById(creatorId)).willReturn(false)

        //when then
        Assertions.assertThatThrownBy {
            subscribeProcessor.subscribe(userId, creatorId)
        }
            .isInstanceOf(CoreException::class.java)
            .hasFieldOrPropertyWithValue("error", Error.NOT_FOUND)
    }

    @Test
    fun `이미 구독한 크리에이터를 다시 구독할 수 없다`() {
        //given
        given(creatorQueryAPI.isExistsById(creatorId)).willReturn(true)
        subscribeProcessor.subscribe(userId, creatorId)

        //when then
        Assertions.assertThatThrownBy {
            subscribeProcessor.subscribe(userId, creatorId)
        }
            .isInstanceOf(CoreException::class.java)
            .hasFieldOrPropertyWithValue("error", Error.ALREADY_SUBSCRIBE_CREATOR)
    }

    @Test
    fun `구독을 취소할 수 있다`() {
        //given
        given(creatorQueryAPI.isExistsById(creatorId)).willReturn(true)
        subscribeProcessor.subscribe(userId, creatorId)

        Assertions.assertThat(subscribeRepository.isSubscribed(userId, creatorId)).isTrue()

        //when
        subscribeProcessor.unSubscribe(userId, creatorId)

        //then
        val isSubscribed = subscribeRepository.isSubscribed(userId, creatorId)
        Assertions.assertThat(isSubscribed).isFalse()
    }
}
