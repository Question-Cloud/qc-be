package com.eager.questioncloud.application.api.creator.service

import com.eager.questioncloud.application.utils.DBCleaner
import com.eager.questioncloud.application.utils.fixture.Fixture
import com.eager.questioncloud.application.utils.fixture.helper.CreatorFixtureHelper
import com.eager.questioncloud.application.utils.fixture.helper.CreatorStatisticsFixtureHelper
import com.eager.questioncloud.application.utils.fixture.helper.UserFixtureHelper
import com.eager.questioncloud.core.domain.creator.infrastructure.repository.CreatorRepository
import com.eager.questioncloud.core.domain.creator.infrastructure.repository.CreatorStatisticsRepository
import com.eager.questioncloud.core.domain.subscribe.infrastructure.repository.SubscribeRepository
import com.eager.questioncloud.core.domain.subscribe.model.Subscribe
import com.eager.questioncloud.core.domain.user.enums.UserStatus
import com.eager.questioncloud.core.domain.user.infrastructure.repository.UserRepository
import com.navercorp.fixturemonkey.kotlin.giveMeKotlinBuilder
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
class CreatorInformationServiceTest(
    @Autowired val creatorInformationService: CreatorInformationService,
    @Autowired val userRepository: UserRepository,
    @Autowired val creatorRepository: CreatorRepository,
    @Autowired val creatorStatisticsRepository: CreatorStatisticsRepository,
    @Autowired val subscribeRepository: SubscribeRepository,
    @Autowired val dbCleaner: DBCleaner,
) {
    @AfterEach
    fun tearDown() {
        dbCleaner.cleanUp()
    }

    @Test
    fun `크리에이터 정보를 조회할 수 있다`() {
        //given
        val user = UserFixtureHelper.createEmailUser("user1@naver.com", "qwer1234", UserStatus.Active, userRepository)
        val creator = CreatorFixtureHelper.createCreator(user.uid, creatorRepository)
        val creatorStatistics =
            CreatorStatisticsFixtureHelper.createCreatorStatistics(creator.id, creatorStatisticsRepository)
        val subscribersCount = 10
        createDummySubscribers(creator.id, subscribersCount)

        // when
        val creatorInformation = creatorInformationService.getCreatorInformation(creator.id)

        //then
        Assertions.assertThat(creatorInformation)
            .extracting(
                { it.creatorProfile.name },
                { it.salesCount },
                { it.averageRateOfReview },
                { it.subscriberCount })
            .containsExactly(
                user.userInformation.name,
                creatorStatistics.salesCount,
                creatorStatistics.averageRateOfReview,
                subscribersCount
            )
    }

    fun createDummySubscribers(creatorId: Long, count: Int) {
        val dummySubscribe = mutableListOf<Subscribe>()

        for (i in 1..count) {
            dummySubscribe.add(
                Fixture.fixtureMonkey.giveMeKotlinBuilder<Subscribe>()
                    .set(Subscribe::creatorId, creatorId)
                    .set(Subscribe::subscriberId, i)
                    .sample()
            )
        }

        dummySubscribe.forEach {
            subscribeRepository.save(Subscribe.create(it.subscriberId, it.creatorId))
        }
    }
}