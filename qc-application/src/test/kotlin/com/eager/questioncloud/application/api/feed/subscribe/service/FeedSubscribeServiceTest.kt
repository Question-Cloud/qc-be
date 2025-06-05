package com.eager.questioncloud.application.api.feed.subscribe.service

import com.eager.questioncloud.application.utils.DBCleaner
import com.eager.questioncloud.application.utils.fixture.helper.CreatorFixtureHelper
import com.eager.questioncloud.application.utils.fixture.helper.CreatorStatisticsFixtureHelper
import com.eager.questioncloud.application.utils.fixture.helper.UserFixtureHelper
import com.eager.questioncloud.core.common.PagingInformation
import com.eager.questioncloud.core.domain.creator.infrastructure.repository.CreatorRepository
import com.eager.questioncloud.core.domain.creator.infrastructure.repository.CreatorStatisticsRepository
import com.eager.questioncloud.core.domain.creator.model.Creator
import com.eager.questioncloud.core.domain.subscribe.infrastructure.repository.SubscribeRepository
import com.eager.questioncloud.core.domain.subscribe.model.Subscribe
import com.eager.questioncloud.core.domain.user.enums.UserStatus
import com.eager.questioncloud.core.domain.user.infrastructure.repository.UserRepository
import com.eager.questioncloud.core.domain.user.model.User
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
class FeedSubscribeServiceTest(
    @Autowired private val feedSubscribeService: FeedSubscribeService,
    @Autowired private val userRepository: UserRepository,
    @Autowired private val creatorRepository: CreatorRepository,
    @Autowired private val creatorStatisticsRepository: CreatorStatisticsRepository,
    @Autowired private val subscribeRepository: SubscribeRepository,
    @Autowired private val dbCleaner: DBCleaner,
) {
    @AfterEach
    fun tearDown() {
        dbCleaner.cleanUp()
    }

    @Test
    fun `구독중인 크리에이터 정보를 조회할 수 있다`() {
        // given
        val user = UserFixtureHelper.createDefaultEmailUser(userRepository)
        val dummy = dummyUserAndCreators(10)

        val dummyCreatorUsers = dummy.first
        val dummyCreators = dummy.second

        dummyMySubscribe(user.uid, dummyCreators)

        // when
        val creatorInformations = feedSubscribeService.getMySubscribes(user.uid, PagingInformation(0, 10))

        // then
        Assertions.assertThat(creatorInformations.size).isEqualTo(dummyCreators.size)

        creatorInformations.forEach { creatorInfo ->
            val creator = dummyCreators.first { it.id == creatorInfo.creatorProfile.creatorId }
            val creatorId = creator.id
            val creatorUserId = dummyCreators.first { it.id == creatorId }.userId
            val creatorUser = dummyCreatorUsers.first { it.uid == creatorUserId }

            Assertions.assertThat(creatorInfo.creatorProfile.name).isEqualTo(creatorUser.userInformation.name)
            Assertions.assertThat(creatorInfo.creatorProfile.email).isEqualTo(creatorUser.userInformation.email)
            Assertions.assertThat(creatorInfo.creatorProfile.mainSubject).isEqualTo(creator.mainSubject)
            Assertions.assertThat(creatorInfo.creatorProfile.introduction).isEqualTo(creator.introduction)
            Assertions.assertThat(creatorInfo.subscriberCount).isEqualTo(1)
        }
    }

    @Test
    fun `구독중인 크리에이터 수를 조회할 수 있다`() {
        // given
        val user = UserFixtureHelper.createDefaultEmailUser(userRepository)
        val subscribedCount = 28
        val dummy = dummyUserAndCreators(subscribedCount)
        val dummyCreators = dummy.second
        dummyMySubscribe(user.uid, dummyCreators)

        // when
        val result = feedSubscribeService.countMySubscribe(user.uid)

        // then
        Assertions.assertThat(result).isEqualTo(subscribedCount)
    }


    private fun dummyUserAndCreators(count: Int): Pair<List<User>, List<Creator>> {
        val users = mutableListOf<User>()
        val creators = mutableListOf<Creator>()

        for (i in 1..count) {
            val dummyUser = UserFixtureHelper.createEmailUser(
                "user${i}@dummy.com",
                "password",
                UserStatus.Active,
                userRepository
            )
            val creator = CreatorFixtureHelper.createCreator(dummyUser.uid, creatorRepository)
            CreatorStatisticsFixtureHelper.createCreatorStatistics(creator.id, creatorStatisticsRepository)
            users.add(dummyUser)
            creators.add(creator)
        }
        return Pair(users, creators)
    }

    private fun dummyMySubscribe(uid: Long, dummyCreators: List<Creator>) {
        dummyCreators.forEach { creator ->
            subscribeRepository.save(Subscribe.create(uid, creator.id))
        }
    }
}