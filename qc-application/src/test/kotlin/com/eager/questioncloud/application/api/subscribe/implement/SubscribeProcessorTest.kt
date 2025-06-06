package com.eager.questioncloud.application.api.subscribe.implement

import com.eager.questioncloud.application.utils.DBCleaner
import com.eager.questioncloud.application.utils.fixture.helper.CreatorFixtureHelper
import com.eager.questioncloud.application.utils.fixture.helper.SubscribeFixtureHelper
import com.eager.questioncloud.application.utils.fixture.helper.UserFixtureHelper
import com.eager.questioncloud.core.domain.creator.infrastructure.repository.CreatorRepository
import com.eager.questioncloud.core.domain.subscribe.infrastructure.repository.SubscribeRepository
import com.eager.questioncloud.core.domain.user.enums.UserStatus
import com.eager.questioncloud.core.domain.user.infrastructure.repository.UserRepository
import com.eager.questioncloud.core.exception.CoreException
import com.eager.questioncloud.core.exception.Error
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
class SubscribeProcessorTest(
    @Autowired val subscribeProcessor: SubscribeProcessor,
    @Autowired val userRepository: UserRepository,
    @Autowired val creatorRepository: CreatorRepository,
    @Autowired val subscribeRepository: SubscribeRepository,
    @Autowired val dbCleaner: DBCleaner,
) {
    @AfterEach
    fun tearDown() {
        dbCleaner.cleanUp()
    }

    @Test
    fun `사용자는 크리에이터를 구독할 수 있다`() {
        //given
        val subscriberUser =
            UserFixtureHelper.createEmailUser("subscriber@test.com", "password123", UserStatus.Active, userRepository)
        val creatorUser =
            UserFixtureHelper.createEmailUser("creator@test.com", "password123", UserStatus.Active, userRepository)
        val creator = CreatorFixtureHelper.createCreator(creatorUser.uid, creatorRepository)

        //when
        subscribeProcessor.subscribe(subscriberUser.uid, creator.id)

        //then
        val isSubscribed = subscribeRepository.isSubscribed(subscriberUser.uid, creator.id)
        Assertions.assertThat(isSubscribed).isTrue()
    }

    @Test
    fun `존재하지 않는 크리에이터를 구독하려고 하면 예외가 발생한다`() {
        //given
        val subscriberUser =
            UserFixtureHelper.createEmailUser("subscriber@test.com", "password123", UserStatus.Active, userRepository)
        val nonExistentCreatorId = 999999L

        //when & then
        val exception = assertThrows<CoreException> {
            subscribeProcessor.subscribe(subscriberUser.uid, nonExistentCreatorId)
        }
        Assertions.assertThat(exception.error).isEqualTo(Error.NOT_FOUND)
    }

    @Test
    fun `이미 구독한 크리에이터를 다시 구독하려고 하면 예외가 발생한다`() {
        //given
        val subscriberUser =
            UserFixtureHelper.createEmailUser("subscriber@test.com", "password123", UserStatus.Active, userRepository)
        val creatorUser =
            UserFixtureHelper.createEmailUser("creator@test.com", "password123", UserStatus.Active, userRepository)
        val creator = CreatorFixtureHelper.createCreator(creatorUser.uid, creatorRepository)

        // 이미 구독된 상태
        SubscribeFixtureHelper.createSubscribe(subscriberUser.uid, creator.id, subscribeRepository)

        //when & then
        val exception = assertThrows<CoreException> {
            subscribeProcessor.subscribe(subscriberUser.uid, creator.id)
        }
        Assertions.assertThat(exception.error).isEqualTo(Error.ALREADY_SUBSCRIBE_CREATOR)
    }

    @Test
    fun `사용자는 구독한 크리에이터를 구독 취소할 수 있다`() {
        //given
        val subscriberUser =
            UserFixtureHelper.createEmailUser("subscriber@test.com", "password123", UserStatus.Active, userRepository)
        val creatorUser =
            UserFixtureHelper.createEmailUser("creator@test.com", "password123", UserStatus.Active, userRepository)
        val creator = CreatorFixtureHelper.createCreator(creatorUser.uid, creatorRepository)

        // 먼저 구독
        SubscribeFixtureHelper.createSubscribe(subscriberUser.uid, creator.id, subscribeRepository)

        // 구독 상태 확인
        val isSubscribedBefore = subscribeRepository.isSubscribed(subscriberUser.uid, creator.id)
        Assertions.assertThat(isSubscribedBefore).isTrue()

        //when
        subscribeProcessor.unSubscribe(subscriberUser.uid, creator.id)

        //then
        val isSubscribedAfter = subscribeRepository.isSubscribed(subscriberUser.uid, creator.id)
        Assertions.assertThat(isSubscribedAfter).isFalse()
    }

    @Test
    fun `여러 사용자가 같은 크리에이터를 구독할 수 있다`() {
        //given
        val subscriber1User =
            UserFixtureHelper.createEmailUser("subscriber1@test.com", "password123", UserStatus.Active, userRepository)
        val subscriber2User =
            UserFixtureHelper.createEmailUser("subscriber2@test.com", "password123", UserStatus.Active, userRepository)
        val creatorUser =
            UserFixtureHelper.createEmailUser("creator@test.com", "password123", UserStatus.Active, userRepository)
        val creator = CreatorFixtureHelper.createCreator(creatorUser.uid, creatorRepository)

        //when
        subscribeProcessor.subscribe(subscriber1User.uid, creator.id)
        subscribeProcessor.subscribe(subscriber2User.uid, creator.id)

        //then
        val isSubscriber1Subscribed = subscribeRepository.isSubscribed(subscriber1User.uid, creator.id)
        val isSubscriber2Subscribed = subscribeRepository.isSubscribed(subscriber2User.uid, creator.id)

        Assertions.assertThat(isSubscriber1Subscribed).isTrue()
        Assertions.assertThat(isSubscriber2Subscribed).isTrue()
    }

    @Test
    fun `한 사용자가 여러 크리에이터를 구독할 수 있다`() {
        //given
        val subscriberUser =
            UserFixtureHelper.createEmailUser("subscriber@test.com", "password123", UserStatus.Active, userRepository)

        val creator1User =
            UserFixtureHelper.createEmailUser("creator1@test.com", "password123", UserStatus.Active, userRepository)
        val creator2User =
            UserFixtureHelper.createEmailUser("creator2@test.com", "password123", UserStatus.Active, userRepository)

        val creator1 = CreatorFixtureHelper.createCreator(creator1User.uid, creatorRepository)
        val creator2 = CreatorFixtureHelper.createCreator(creator2User.uid, creatorRepository)

        //when
        subscribeProcessor.subscribe(subscriberUser.uid, creator1.id)
        subscribeProcessor.subscribe(subscriberUser.uid, creator2.id)

        //then
        val isCreator1Subscribed = subscribeRepository.isSubscribed(subscriberUser.uid, creator1.id)
        val isCreator2Subscribed = subscribeRepository.isSubscribed(subscriberUser.uid, creator2.id)

        Assertions.assertThat(isCreator1Subscribed).isTrue()
        Assertions.assertThat(isCreator2Subscribed).isTrue()
    }
}
