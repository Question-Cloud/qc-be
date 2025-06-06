package com.eager.questioncloud.application.api.subscribe.service

import com.eager.questioncloud.application.utils.DBCleaner
import com.eager.questioncloud.application.utils.fixture.helper.CreatorFixtureHelper
import com.eager.questioncloud.application.utils.fixture.helper.SubscribeFixtureHelper
import com.eager.questioncloud.application.utils.fixture.helper.UserFixtureHelper
import com.eager.questioncloud.core.domain.creator.infrastructure.repository.CreatorRepository
import com.eager.questioncloud.core.domain.subscribe.infrastructure.repository.SubscribeRepository
import com.eager.questioncloud.core.domain.user.enums.UserStatus
import com.eager.questioncloud.core.domain.user.infrastructure.repository.UserRepository
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
class SubscribeServiceTest(
    @Autowired val subscribeService: SubscribeService,
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
        val subscriberUser = UserFixtureHelper.createEmailUser("subscriber@test.com", "password123", UserStatus.Active, userRepository)
        val creatorUser = UserFixtureHelper.createEmailUser("creator@test.com", "password123", UserStatus.Active, userRepository)
        val creator = CreatorFixtureHelper.createCreator(creatorUser.uid, creatorRepository)

        //when
        subscribeService.subscribe(subscriberUser.uid, creator.id)

        //then
        val isSubscribed = subscribeRepository.isSubscribed(subscriberUser.uid, creator.id)
        Assertions.assertThat(isSubscribed).isTrue()
    }

    @Test
    fun `사용자는 크리에이터를 구독 취소할 수 있다`() {
        //given
        val subscriberUser = UserFixtureHelper.createEmailUser("subscriber@test.com", "password123", UserStatus.Active, userRepository)
        val creatorUser = UserFixtureHelper.createEmailUser("creator@test.com", "password123", UserStatus.Active, userRepository)
        val creator = CreatorFixtureHelper.createCreator(creatorUser.uid, creatorRepository)
        
        SubscribeFixtureHelper.createSubscribe(subscriberUser.uid, creator.id, subscribeRepository)

        //when
        subscribeService.unSubscribe(subscriberUser.uid, creator.id)

        //then
        val isSubscribed = subscribeRepository.isSubscribed(subscriberUser.uid, creator.id)
        Assertions.assertThat(isSubscribed).isFalse()
    }

    @Test
    fun `구독 여부를 확인할 수 있다`() {
        //given
        val subscriberUser = UserFixtureHelper.createEmailUser("subscriber@test.com", "password123", UserStatus.Active, userRepository)
        val creatorUser = UserFixtureHelper.createEmailUser("creator@test.com", "password123", UserStatus.Active, userRepository)
        val creator = CreatorFixtureHelper.createCreator(creatorUser.uid, creatorRepository)
        
        SubscribeFixtureHelper.createSubscribe(subscriberUser.uid, creator.id, subscribeRepository)

        //when
        val isSubscribed = subscribeService.isSubscribed(subscriberUser.uid, creator.id)

        //then
        Assertions.assertThat(isSubscribed).isTrue()
    }

    @Test
    fun `구독하지 않은 크리에이터의 구독 여부는 false로 반환된다`() {
        //given
        val subscriberUser = UserFixtureHelper.createEmailUser("subscriber@test.com", "password123", UserStatus.Active, userRepository)
        val creatorUser = UserFixtureHelper.createEmailUser("creator@test.com", "password123", UserStatus.Active, userRepository)
        val creator = CreatorFixtureHelper.createCreator(creatorUser.uid, creatorRepository)

        //when
        val isSubscribed = subscribeService.isSubscribed(subscriberUser.uid, creator.id)

        //then
        Assertions.assertThat(isSubscribed).isFalse()
    }

    @Test
    fun `크리에이터의 구독자 수를 조회할 수 있다`() {
        //given
        val subscriber1User = UserFixtureHelper.createEmailUser("subscriber1@test.com", "password123", UserStatus.Active, userRepository)
        val subscriber2User = UserFixtureHelper.createEmailUser("subscriber2@test.com", "password123", UserStatus.Active, userRepository)
        val creatorUser = UserFixtureHelper.createEmailUser("creator@test.com", "password123", UserStatus.Active, userRepository)
        val creator = CreatorFixtureHelper.createCreator(creatorUser.uid, creatorRepository)
        
        SubscribeFixtureHelper.createSubscribe(subscriber1User.uid, creator.id, subscribeRepository)
        SubscribeFixtureHelper.createSubscribe(subscriber2User.uid, creator.id, subscribeRepository)

        //when
        val subscriberCount = subscribeService.countSubscriber(creator.id)

        //then
        Assertions.assertThat(subscriberCount).isGreaterThanOrEqualTo(2)
    }
}
