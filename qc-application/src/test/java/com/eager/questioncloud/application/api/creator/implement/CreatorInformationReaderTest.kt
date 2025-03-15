package com.eager.questioncloud.application.api.creator.implement

import com.eager.questioncloud.application.utils.Fixture
import com.eager.questioncloud.core.domain.creator.infrastructure.repository.CreatorRepository
import com.eager.questioncloud.core.domain.creator.infrastructure.repository.CreatorStatisticsRepository
import com.eager.questioncloud.core.domain.creator.model.Creator
import com.eager.questioncloud.core.domain.creator.model.CreatorStatistics
import com.eager.questioncloud.core.domain.question.enums.Subject
import com.eager.questioncloud.core.domain.subscribe.infrastructure.repository.SubscribeRepository
import com.eager.questioncloud.core.domain.subscribe.model.Subscribe
import com.eager.questioncloud.core.domain.user.infrastructure.repository.UserRepository
import com.eager.questioncloud.core.domain.user.model.User
import com.navercorp.fixturemonkey.kotlin.giveMeKotlinBuilder
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
class CreatorInformationReaderTest(
    @Autowired val creatorInformationReader: CreatorInformationReader,
    @Autowired val userRepository: UserRepository,
    @Autowired val creatorRepository: CreatorRepository,
    @Autowired val creatorStatisticsRepository: CreatorStatisticsRepository,
    @Autowired val subscribeRepository: SubscribeRepository,
) {

    @AfterEach
    fun tearDown() {
        userRepository.deleteAllInBatch()
        creatorRepository.deleteAllInBatch()
        creatorStatisticsRepository.deleteAllInBatch()
        subscribeRepository.deleteAllInBatch()
    }

    @Test
    fun `한 명의 크리에이터 정보를 조회할 수 있다`() {
        //given
        val user = userRepository.save(
            Fixture.fixtureMonkey.giveMeKotlinBuilder<User>()
                .set(User::uid, null)
                .build()
                .sample()
        )

        val creator = creatorRepository.save(Creator.create(user.uid!!, Subject.Biology, "Hello"))

        val creatorStatistics = Fixture.fixtureMonkey.giveMeKotlinBuilder<CreatorStatistics>()
            .set(CreatorStatistics::creatorId, creator.id)
            .sample()

        creatorStatisticsRepository.save(creatorStatistics)

        val subscribers = Fixture.fixtureMonkey.giveMeKotlinBuilder<Subscribe>()
            .set(Subscribe::creatorId, creator.id)
            .sampleList(10)

        subscribers.forEach { subscriber ->
            subscribeRepository.save(subscriber)
        }

        // when
        val creatorInformation = creatorInformationReader.getCreatorInformation(creator.id!!)

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
                subscribers.size
            )
    }

    @Test
    fun `2명 이상의 크리에이터 정보를 조회할 수 있다`() {
        val user1 = userRepository.save(
            Fixture.fixtureMonkey.giveMeKotlinBuilder<User>()
                .set(User::uid, null)
                .build()
                .sample()
        )
        val user2 = userRepository.save(
            Fixture.fixtureMonkey.giveMeKotlinBuilder<User>()
                .set(User::uid, null)
                .build()
                .sample()
        )

        val creator1 = creatorRepository.save(Creator.create(user1.uid!!, Subject.Biology, "Hello"))
        val creator2 = creatorRepository.save(Creator.create(user2.uid!!, Subject.Biology, "Hello"))

        val creatorStatistics1 = Fixture.fixtureMonkey.giveMeKotlinBuilder<CreatorStatistics>()
            .set(CreatorStatistics::creatorId, creator1.id)
            .sample()
        val creatorStatistics2 = Fixture.fixtureMonkey.giveMeKotlinBuilder<CreatorStatistics>()
            .set(CreatorStatistics::creatorId, creator2.id)
            .sample()

        creatorStatisticsRepository.save(creatorStatistics1)
        creatorStatisticsRepository.save(creatorStatistics2)

        val subscribers1 = Fixture.fixtureMonkey.giveMeKotlinBuilder<Subscribe>()
            .set(Subscribe::creatorId, creator1.id)
            .sampleList(10)

        subscribers1.forEach { subscriber ->
            subscribeRepository.save(subscriber)
        }

        val subscribers2 = Fixture.fixtureMonkey.giveMeKotlinBuilder<Subscribe>()
            .set(Subscribe::creatorId, creator2.id)
            .sampleList(10)

        subscribers2.forEach { subscriber ->
            subscribeRepository.save(subscriber)
        }

        // when
        val creatorInformations = creatorInformationReader.getCreatorInformation(listOf(creator1.id!!, creator2.id!!))

        // then
        Assertions.assertThat(creatorInformations.size).isEqualTo(2)

        Assertions.assertThat(creatorInformations[0])
            .extracting(
                { it.creatorProfile.name },
                { it.salesCount },
                { it.averageRateOfReview },
                { it.subscriberCount })
            .containsExactly(
                user1.userInformation.name,
                creatorStatistics1.salesCount,
                creatorStatistics1.averageRateOfReview,
                subscribers1.size
            )

        Assertions.assertThat(creatorInformations[1])
            .extracting(
                { it.creatorProfile.name },
                { it.salesCount },
                { it.averageRateOfReview },
                { it.subscriberCount })
            .containsExactly(
                user2.userInformation.name,
                creatorStatistics2.salesCount,
                creatorStatistics2.averageRateOfReview,
                subscribers2.size
            )
    }
}