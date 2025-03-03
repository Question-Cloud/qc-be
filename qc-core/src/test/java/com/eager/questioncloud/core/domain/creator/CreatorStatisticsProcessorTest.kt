package com.eager.questioncloud.core.domain.creator

import com.eager.questioncloud.core.domain.creator.implement.CreatorStatisticsProcessor
import com.eager.questioncloud.core.domain.creator.infrastructure.repository.CreatorRepository
import com.eager.questioncloud.core.domain.creator.infrastructure.repository.CreatorStatisticsRepository
import com.eager.questioncloud.core.domain.creator.model.Creator
import com.eager.questioncloud.core.domain.creator.model.CreatorStatistics
import com.eager.questioncloud.core.domain.question.enums.QuestionStatus
import com.eager.questioncloud.core.domain.question.infrastructure.repository.QuestionRepository
import com.eager.questioncloud.core.domain.question.model.Question
import com.eager.questioncloud.core.domain.review.event.DeletedReviewEvent
import com.eager.questioncloud.core.domain.review.event.ModifiedReviewEvent
import com.eager.questioncloud.core.domain.review.event.RegisteredReviewEvent
import com.eager.questioncloud.utils.Fixture
import com.navercorp.fixturemonkey.kotlin.giveMeKotlinBuilder
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors

@SpringBootTest
@ActiveProfiles("test")
internal class CreatorStatisticsProcessorTest(
    @Autowired
    private val creatorStatisticsProcessor: CreatorStatisticsProcessor,

    @Autowired
    private val questionRepository: QuestionRepository,

    @Autowired
    private val creatorStatisticsRepository: CreatorStatisticsRepository,

    @Autowired
    private val creatorRepository: CreatorRepository,
) {
    @AfterEach
    fun tearDown() {
        questionRepository.deleteAllInBatch()
        creatorRepository.deleteAllInBatch()
        creatorStatisticsRepository.deleteAllInBatch()
    }

    @Test
    @DisplayName("리뷰 등록 이벤트 크리에이터 평점 통계 업데이트 동시성 이슈 테스트")
    fun creatorStatisticsConcurrencyTestWhenRegisteredReviewEvent() {
        //given
        val creator = creatorRepository.save(
            Fixture.fixtureMonkey.giveMeKotlinBuilder<Creator>()
                .set(Creator::id, null)
                .build()
                .sample()
        )
        creatorStatisticsRepository.save(
            Fixture.fixtureMonkey.giveMeKotlinBuilder<CreatorStatistics>()
                .set(CreatorStatistics::creatorId, creator.id)
                .set(CreatorStatistics::reviewCount, 0)
                .set(CreatorStatistics::totalReviewRate, 0)
                .build()
                .sample()
        )

        val question = questionRepository.save(
            Fixture.fixtureMonkey.giveMeKotlinBuilder<Question>()
                .set(Question::creatorId, creator.id)
                .set(Question::questionStatus, QuestionStatus.Available)
                .build()
                .sample()
        )

        val event = RegisteredReviewEvent.create(question.id!!, 4)

        //when
        val threadCount = 100
        val executorService = Executors.newFixedThreadPool(threadCount)
        val latch = CountDownLatch(threadCount)

        for (i in 0..<threadCount) {
            executorService.execute {
                try {
                    creatorStatisticsProcessor.updateCreatorReviewStatistics(event)
                } catch (ignored: Exception) {
                } finally {
                    latch.countDown()
                }
            }
        }

        latch.await()

        //then
        val creatorStatistics = creatorStatisticsRepository.findByCreatorId(creator.id!!)

        Assertions.assertThat(creatorStatistics.totalReviewRate).isEqualTo(400)
        Assertions.assertThat(creatorStatistics.averageRateOfReview).isEqualTo(4.0)
    }

    @Test
    @DisplayName("리뷰 수정 이벤트 크리에이터 평점 통계 업데이트 동시성 이슈 테스트")
    fun creatorStatisticsConcurrencyTestWhenModifiedReviewEvent() {
        val creator = creatorRepository.save(
            Fixture.fixtureMonkey.giveMeKotlinBuilder<Creator>()
                .set(Creator::id, null)
                .build()
                .sample()
        )
        creatorStatisticsRepository.save(
            Fixture.fixtureMonkey.giveMeKotlinBuilder<CreatorStatistics>()
                .set(CreatorStatistics::creatorId, creator.id)
                .set(CreatorStatistics::reviewCount, 100)
                .set(CreatorStatistics::totalReviewRate, 0)
                .build()
                .sample()
        )

        val question = questionRepository.save(
            Fixture.fixtureMonkey.giveMeKotlinBuilder<Question>()
                .set(Question::creatorId, creator.id)
                .set(Question::questionStatus, QuestionStatus.Available)
                .build()
                .sample()
        )

        val event = ModifiedReviewEvent.create(question.id!!, 3)

        //when
        val threadCount = 100
        val executorService = Executors.newFixedThreadPool(threadCount)
        val latch = CountDownLatch(threadCount)

        for (i in 0..<threadCount) {
            executorService.execute {
                try {
                    creatorStatisticsProcessor.updateCreatorReviewStatistics(event)
                } catch (ignored: Exception) {
                } finally {
                    latch.countDown()
                }
            }
        }

        latch.await()

        //then
        val creatorStatistics = creatorStatisticsRepository.findByCreatorId(creator.id!!)

        Assertions.assertThat(creatorStatistics.totalReviewRate).isEqualTo(300)
        Assertions.assertThat(creatorStatistics.averageRateOfReview).isEqualTo(3.0)
    }

    @Test
    @DisplayName("리뷰 삭제 이벤트 크리에이터 평점 통계 업데이트 동시성 이슈 테스트")
    fun creatorStatisticsConcurrencyTestWhenDeletedReviewEvent() {
        val creator = creatorRepository.save(
            Fixture.fixtureMonkey.giveMeKotlinBuilder<Creator>()
                .set(Creator::id, null)
                .build()
                .sample()
        )
        creatorStatisticsRepository.save(
            Fixture.fixtureMonkey.giveMeKotlinBuilder<CreatorStatistics>()
                .set(CreatorStatistics::creatorId, creator.id)
                .set(CreatorStatistics::reviewCount, 100)
                .set(CreatorStatistics::totalReviewRate, 400)
                .build()
                .sample()
        )

        val question = questionRepository.save(
            Fixture.fixtureMonkey.giveMeKotlinBuilder<Question>()
                .set(Question::creatorId, creator.id)
                .set(Question::questionStatus, QuestionStatus.Available)
                .build()
                .sample()
        )

        val event = DeletedReviewEvent.create(question.id!!, 4)

        //when
        val threadCount = 80
        val executorService = Executors.newFixedThreadPool(threadCount)
        val latch = CountDownLatch(threadCount)

        for (i in 0..<threadCount) {
            executorService.execute {
                try {
                    creatorStatisticsProcessor.updateCreatorReviewStatistics(event)
                } catch (ignored: Exception) {
                } finally {
                    latch.countDown()
                }
            }
        }

        latch.await()

        //then
        val creatorStatistics = creatorStatisticsRepository.findByCreatorId(creator.id!!)

        Assertions.assertThat(creatorStatistics.totalReviewRate).isEqualTo(80)
        Assertions.assertThat(creatorStatistics.reviewCount).isEqualTo(20)
        Assertions.assertThat(creatorStatistics.averageRateOfReview).isEqualTo(4.0)
    }

    @Test
    @DisplayName("리뷰 추가, 수정, 삭제 이벤트가 동시에 일어나는 경우 크리에이터 통계 업데이트 동시성 테스트")
    fun creatorStatisticsConcurrencyTestWhenMultipleEvent() {
        //given
        val creator = creatorRepository.save(
            Fixture.fixtureMonkey.giveMeKotlinBuilder<Creator>()
                .set(Creator::id, null)
                .build()
                .sample()
        )
        creatorStatisticsRepository.save(
            Fixture.fixtureMonkey.giveMeKotlinBuilder<CreatorStatistics>()
                .set(CreatorStatistics::creatorId, creator.id)
                .set(CreatorStatistics::reviewCount, 100)
                .set(CreatorStatistics::totalReviewRate, 100)
                .set(CreatorStatistics::averageRateOfReview, 1.0)
                .build()
                .sample()
        )

        val question = questionRepository.save(
            Fixture.fixtureMonkey.giveMeKotlinBuilder<Question>()
                .set(Question::creatorId, creator.id)
                .set(Question::questionStatus, QuestionStatus.Available)
                .build()
                .sample()
        )

        val registeredReviewEvent = RegisteredReviewEvent.create(question.id!!, 1)
        val modifiedReviewEvent = ModifiedReviewEvent.create(question.id!!, 1)
        val deletedReviewEvent = DeletedReviewEvent.create(question.id!!, 1)

        //when
        val threadCount = 120
        val executorService = Executors.newFixedThreadPool(threadCount)
        val latch = CountDownLatch(threadCount)

        for (i in 0..39) {
            executorService.execute {
                try {
                    creatorStatisticsProcessor.updateCreatorReviewStatistics(registeredReviewEvent)
                } catch (ignored: Exception) {
                } finally {
                    latch.countDown()
                }
            }
        }

        for (i in 0..39) {
            executorService.execute {
                try {
                    creatorStatisticsProcessor.updateCreatorReviewStatistics(modifiedReviewEvent)
                } catch (ignored: Exception) {
                } finally {
                    latch.countDown()
                }
            }
        }

        for (i in 0..39) {
            executorService.execute {
                try {
                    creatorStatisticsProcessor.updateCreatorReviewStatistics(deletedReviewEvent)
                } catch (ignored: Exception) {
                } finally {
                    latch.countDown()
                }
            }
        }

        latch.await()

        //then
        val creatorStatistics = creatorStatisticsRepository.findByCreatorId(creator.id!!)
        Assertions.assertThat(creatorStatistics.totalReviewRate).isEqualTo(140)
        Assertions.assertThat(creatorStatistics.reviewCount).isEqualTo(100)
        Assertions.assertThat(creatorStatistics.averageRateOfReview).isEqualTo(1.4)
    }
}