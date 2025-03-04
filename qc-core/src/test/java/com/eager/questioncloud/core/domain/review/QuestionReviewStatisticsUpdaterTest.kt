package com.eager.questioncloud.core.domain.review

import com.eager.questioncloud.core.domain.question.enums.QuestionStatus
import com.eager.questioncloud.core.domain.question.infrastructure.repository.QuestionRepository
import com.eager.questioncloud.core.domain.question.model.Question
import com.eager.questioncloud.core.domain.review.event.DeletedReviewEvent
import com.eager.questioncloud.core.domain.review.event.ModifiedReviewEvent
import com.eager.questioncloud.core.domain.review.event.RegisteredReviewEvent
import com.eager.questioncloud.core.domain.review.implement.QuestionReviewStatisticsUpdater
import com.eager.questioncloud.core.domain.review.infrastructure.repository.QuestionReviewStatisticsRepository
import com.eager.questioncloud.core.domain.review.model.QuestionReviewStatistics
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
internal class QuestionReviewStatisticsUpdaterTest(
    @Autowired
    private val questionReviewStatisticsUpdater: QuestionReviewStatisticsUpdater,

    @Autowired
    private val questionRepository: QuestionRepository,

    @Autowired
    private val questionReviewStatisticsRepository: QuestionReviewStatisticsRepository,
) {
    @AfterEach
    fun tearDown() {
        questionRepository.deleteAllInBatch()
        questionReviewStatisticsRepository.deleteAllInBatch()
    }

    @Test
    @DisplayName("리뷰 작성 이벤트 평점 통계 업데이트 동시성 테스트")
    @Throws(
        InterruptedException::class
    )
    fun reviewStatisticsConcurrencyTestWhenRegisteredReviewEvent() {
        //given
        val question = questionRepository.save(
            Fixture.fixtureMonkey.giveMeKotlinBuilder<Question>()
                .set(Question::questionStatus, QuestionStatus.Available)
                .build()
                .sample()
        )
        questionReviewStatisticsRepository.save(
            Fixture.fixtureMonkey.giveMeKotlinBuilder<QuestionReviewStatistics>()
                .set(QuestionReviewStatistics::questionId, question.id)
                .set(QuestionReviewStatistics::totalRate, 0)
                .set(QuestionReviewStatistics::reviewCount, 0)
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
                    questionReviewStatisticsUpdater.updateByRegisteredReview(event)
                } catch (ignored: Exception) {
                } finally {
                    latch.countDown()
                }
            }
        }

        latch.await()

        //then
        val questionReviewStatistics = questionReviewStatisticsRepository.get(question.id!!)
        Assertions.assertThat(questionReviewStatistics.reviewCount).isEqualTo(100)
        Assertions.assertThat(questionReviewStatistics.totalRate).isEqualTo(400)
    }

    @Test
    @DisplayName("리뷰 수정 이벤트 평점 통계 업데이트 동시성 테스트")
    @Throws(
        InterruptedException::class
    )
    fun reviewStatisticsConcurrencyTestWhenModifiedReviewEvent() {
        //given
        val question = questionRepository.save(
            Fixture.fixtureMonkey.giveMeKotlinBuilder<Question>()
                .set(Question::questionStatus, QuestionStatus.Available)
                .build()
                .sample()
        )
        questionReviewStatisticsRepository.save(
            Fixture.fixtureMonkey.giveMeKotlinBuilder<QuestionReviewStatistics>()
                .set(QuestionReviewStatistics::questionId, question.id)
                .set(QuestionReviewStatistics::reviewCount, 100)
                .set(QuestionReviewStatistics::totalRate, 0)
                .set(QuestionReviewStatistics::averageRate, 0.0)
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
                    questionReviewStatisticsUpdater.updateByModifiedReview(event)
                } catch (ignored: Exception) {
                } finally {
                    latch.countDown()
                }
            }
        }

        latch.await()

        //then
        val questionReviewStatistics = questionReviewStatisticsRepository.get(question.id!!)
        Assertions.assertThat(questionReviewStatistics.totalRate).isEqualTo(300)
        Assertions.assertThat(questionReviewStatistics.reviewCount).isEqualTo(100)
        Assertions.assertThat(questionReviewStatistics.averageRate).isEqualTo(3.0)
    }

    @Test
    @DisplayName("리뷰 삭제 이벤트 평점 통계 업데이트 동시성 테스트")
    @Throws(
        InterruptedException::class
    )
    fun reviewStatisticsConcurrencyTestWhenDeletedReviewEvent() {
        //given
        val question = questionRepository.save(
            Fixture.fixtureMonkey.giveMeKotlinBuilder<Question>()
                .set(Question::questionStatus, QuestionStatus.Available)
                .build()
                .sample()
        )
        questionReviewStatisticsRepository.save(
            Fixture.fixtureMonkey.giveMeKotlinBuilder<QuestionReviewStatistics>()
                .set(QuestionReviewStatistics::questionId, question.id)
                .set(QuestionReviewStatistics::reviewCount, 100)
                .set(QuestionReviewStatistics::totalRate, 400)
                .set(QuestionReviewStatistics::averageRate, 4.0)
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
                    questionReviewStatisticsUpdater.updateByDeletedReview(event)
                } catch (ignored: Exception) {
                } finally {
                    latch.countDown()
                }
            }
        }

        latch.await()

        //then
        val questionReviewStatistics = questionReviewStatisticsRepository.get(question.id!!)
        Assertions.assertThat(questionReviewStatistics.totalRate).isEqualTo(80)
        Assertions.assertThat(questionReviewStatistics.reviewCount).isEqualTo(20)
        Assertions.assertThat(questionReviewStatistics.averageRate).isEqualTo(4.0)
    }

    @Test
    @DisplayName("리뷰 추가, 수정, 삭제 이벤트가 동시에 일어나는 경우 리뷰 통계 평점 업데이트 동시성 테스트")
    @Throws(
        InterruptedException::class
    )
    fun reviewStatisticsConcurrencyTestWhenMultipleEvent() {
        //given
        val question = questionRepository.save(
            Fixture.fixtureMonkey.giveMeKotlinBuilder<Question>()
                .set(Question::questionStatus, QuestionStatus.Available)
                .build()
                .sample()
        )
        questionReviewStatisticsRepository.save(
            Fixture.fixtureMonkey.giveMeKotlinBuilder<QuestionReviewStatistics>()
                .set(QuestionReviewStatistics::questionId, question.id)
                .set(QuestionReviewStatistics::reviewCount, 100)
                .set(QuestionReviewStatistics::totalRate, 100)
                .set(QuestionReviewStatistics::averageRate, 1.0)
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
                    questionReviewStatisticsUpdater.updateByRegisteredReview(registeredReviewEvent)
                } catch (ignored: Exception) {
                } finally {
                    latch.countDown()
                }
            }
        }

        for (i in 0..39) {
            executorService.execute {
                try {
                    questionReviewStatisticsUpdater.updateByModifiedReview(modifiedReviewEvent)
                } catch (ignored: Exception) {
                } finally {
                    latch.countDown()
                }
            }
        }

        for (i in 0..39) {
            executorService.execute {
                try {
                    questionReviewStatisticsUpdater.updateByDeletedReview(deletedReviewEvent)
                } catch (ignored: Exception) {
                } finally {
                    latch.countDown()
                }
            }
        }

        latch.await()

        //then
        val questionReviewStatistics = questionReviewStatisticsRepository.get(question.id!!)
        Assertions.assertThat(questionReviewStatistics.totalRate).isEqualTo(140)
        Assertions.assertThat(questionReviewStatistics.reviewCount).isEqualTo(100)
        Assertions.assertThat(questionReviewStatistics.averageRate).isEqualTo(1.4)
    }
}