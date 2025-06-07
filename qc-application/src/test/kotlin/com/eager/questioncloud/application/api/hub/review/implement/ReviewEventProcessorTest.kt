package com.eager.questioncloud.application.api.hub.review.implement

import com.eager.questioncloud.application.api.hub.review.event.ReviewEvent
import com.eager.questioncloud.application.api.hub.review.event.ReviewEventType
import com.eager.questioncloud.application.utils.DBCleaner
import com.eager.questioncloud.core.domain.review.infrastructure.repository.QuestionReviewEventLogRepository
import com.eager.questioncloud.core.domain.review.model.QuestionReviewEventLog
import org.assertj.core.api.Assertions
import org.awaitility.Awaitility.await
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.event.ApplicationEvents
import org.springframework.test.context.event.RecordApplicationEvents
import software.amazon.awssdk.services.sns.SnsAsyncClient
import software.amazon.awssdk.services.sns.model.PublishRequest
import software.amazon.awssdk.services.sns.model.PublishResponse
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit

@SpringBootTest
@ActiveProfiles("test")
@RecordApplicationEvents
class ReviewEventProcessorTest(
    @Autowired private val reviewEventProcessor: ReviewEventProcessor,
    @Autowired private val questionReviewEventLogRepository: QuestionReviewEventLogRepository,
    @Autowired private val dbCleaner: DBCleaner
) {
    @MockBean
    private lateinit var snsAsyncClient: SnsAsyncClient

    @Autowired
    lateinit var events: ApplicationEvents;

    @AfterEach
    fun tearDown() {
        dbCleaner.cleanUp()
    }

    @Test
    fun `리뷰 이벤트를 SNS에 발행할 수 있다`() {
        // given
        val dummyQuestionId = 1L
        val varianceRate = 3
        val reviewEventType = ReviewEventType.REGISTER
        val reviewEvent = ReviewEvent.create(dummyQuestionId, varianceRate, reviewEventType)
        questionReviewEventLogRepository.save(QuestionReviewEventLog.create(reviewEvent.eventId, reviewEvent.toJson()))

        given(snsAsyncClient.publish(any<PublishRequest>())).willReturn(
            CompletableFuture.completedFuture(
                PublishResponse.builder().build()
            )
        )

        // when
        reviewEventProcessor.publishEvent(reviewEvent)

        // then
        await().atMost(3, TimeUnit.SECONDS)
            .pollInterval(100, TimeUnit.MILLISECONDS)
            .until {
                questionReviewEventLogRepository.findByEventId(reviewEvent.eventId).isPublish
            }

        val eventLog = questionReviewEventLogRepository.findByEventId(reviewEvent.eventId)
        Assertions.assertThat(eventLog).isNotNull()
        Assertions.assertThat(eventLog.eventId).isEqualTo(reviewEvent.eventId)
        Assertions.assertThat(eventLog.isPublish).isTrue()
    }

    @Test
    fun `리뷰 이벤트 Transactional Outbox 처리를 할 수 있다`() {
        // gvien
        val dummyQuestionId = 1L
        val varianceRate = 3
        val reviewEventType = ReviewEventType.REGISTER
        val reviewEvent = ReviewEvent.create(dummyQuestionId, varianceRate, reviewEventType)
        given(snsAsyncClient.publish(any<PublishRequest>())).willReturn(
            CompletableFuture.completedFuture(
                PublishResponse.builder().build()
            )
        )

        // when
        reviewEventProcessor.saveEventLog(reviewEvent)

        // then
        val eventLog = questionReviewEventLogRepository.findByEventId(reviewEvent.eventId)

        Assertions.assertThat(eventLog.eventId).isEqualTo(reviewEvent.eventId)
        Assertions.assertThat(eventLog.isPublish).isFalse()
        Assertions.assertThat(events.stream(ReviewEvent::class.java).count()).isEqualTo(1)
    }

    @Test
    fun `발행되지 않은 리뷰 이벤트를 조회할 수 있다`() {
        // given
        val dummyQuestionId = 1L
        val varianceRate = 3
        val reviewEventType = ReviewEventType.REGISTER

        val reviewEvent1 = ReviewEvent.create(dummyQuestionId, varianceRate, reviewEventType)
        val reviewEvent2 = ReviewEvent.create(dummyQuestionId, varianceRate, reviewEventType)

        questionReviewEventLogRepository.save(
            QuestionReviewEventLog.create(
                reviewEvent1.eventId,
                reviewEvent1.toJson()
            )
        )
        questionReviewEventLogRepository.save(
            QuestionReviewEventLog.create(
                reviewEvent2.eventId,
                reviewEvent2.toJson()
            )
        )

        // when
        val reviewEventLogs = reviewEventProcessor.getUnpublishedEvents()

        // then
        Assertions.assertThat(reviewEventLogs).hasSize(2)
        Assertions.assertThat(reviewEventLogs)
            .extracting("eventId")
            .containsAll(listOf(reviewEvent1.eventId, reviewEvent2.eventId))
    }
}