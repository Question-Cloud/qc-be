package com.eager.questioncloud.application.api.hub.review.service

import com.eager.questioncloud.application.api.hub.review.event.ReviewEvent
import com.eager.questioncloud.application.utils.DBCleaner
import com.eager.questioncloud.application.utils.fixture.helper.CreatorFixtureHelper
import com.eager.questioncloud.application.utils.fixture.helper.QuestionFixtureHelper
import com.eager.questioncloud.application.utils.fixture.helper.QuestionReviewFixtureHelper
import com.eager.questioncloud.application.utils.fixture.helper.UserFixtureHelper
import com.eager.questioncloud.core.domain.creator.infrastructure.repository.CreatorRepository
import com.eager.questioncloud.core.domain.question.enums.QuestionStatus
import com.eager.questioncloud.core.domain.question.infrastructure.repository.QuestionRepository
import com.eager.questioncloud.core.domain.question.model.Question
import com.eager.questioncloud.core.domain.review.infrastructure.repository.QuestionReviewRepository
import com.eager.questioncloud.core.domain.review.model.QuestionReview
import com.eager.questioncloud.core.domain.user.infrastructure.repository.UserRepository
import com.eager.questioncloud.core.domain.userquestion.infrastructure.repository.UserQuestionRepository
import com.eager.questioncloud.core.domain.userquestion.model.UserQuestion
import com.eager.questioncloud.core.exception.CoreException
import com.eager.questioncloud.core.exception.Error
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito
import org.mockito.kotlin.any
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.SpyBean
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.event.ApplicationEvents
import org.springframework.test.context.event.RecordApplicationEvents
import software.amazon.awssdk.services.sns.SnsClient
import software.amazon.awssdk.services.sns.model.PublishRequest
import software.amazon.awssdk.services.sns.model.PublishResponse

@SpringBootTest
@ActiveProfiles("test")
@RecordApplicationEvents
class HubReviewServiceTest(
    @Autowired private val hubReviewService: HubReviewService,
    @Autowired private val questionReviewRepository: QuestionReviewRepository,
    @Autowired private val userQuestionRepository: UserQuestionRepository,
    @Autowired private val userRepository: UserRepository,
    @Autowired private val creatorRepository: CreatorRepository,
    @Autowired private val questionRepository: QuestionRepository,
    @Autowired @SpyBean private val snsClient: SnsClient,
    @Autowired private val dbCleaner: DBCleaner,
) {
    @Autowired
    lateinit var events: ApplicationEvents

    var creatorId: Long = 0

    @BeforeEach
    fun setUp() {
        val creatorUser = UserFixtureHelper.createDefaultEmailUser(userRepository)
        val creator = CreatorFixtureHelper.createCreator(creatorUser.uid, creatorRepository)
        creatorId = creator.id
    }

    @AfterEach
    fun tearDown() {
        dbCleaner.cleanUp()
    }

    @Test
    fun `작성된 리뷰 개수를 조회할 수 있다`() {
        // given
        val dummyReviewCount = 10
        val questionId = 1L
        for (i in 1..dummyReviewCount) {
            val reviewerId = i.toLong()
            QuestionReviewFixtureHelper.createQuestinoReview(reviewerId, questionId, questionReviewRepository)
        }

        // when
        val reviewCount = hubReviewService.getTotal(questionId)

        // then
        Assertions.assertThat(reviewCount).isEqualTo(dummyReviewCount)
    }

    @Test
    fun `내가 작성한 문제의 리뷰를 조회할 수 있다`() {
        // given
        val questionId = 1L
        val userId = 1L
        val review = QuestionReviewFixtureHelper.createQuestinoReview(userId, questionId, questionReviewRepository)

        // when
        val reviewResult = hubReviewService.getMyQuestionReview(questionId, userId)

        // then
        Assertions.assertThat(reviewResult).isNotNull()
        Assertions.assertThat(reviewResult.comment).isEqualTo(review.comment)
        Assertions.assertThat(reviewResult.rate).isEqualTo(review.rate)
    }

    @Test
    fun `리뷰를 작성하지 않았을 때 내가 작성한 문제의 리뷰를 조회하는 경우 예외가 발생한다`() {
        // given
        val questionId = 1L
        val userId = 1L

        // when then
        Assertions.assertThatThrownBy { hubReviewService.getMyQuestionReview(questionId, userId) }
            .isInstanceOf(CoreException::class.java)
            .hasFieldOrPropertyWithValue("error", Error.NOT_FOUND)
    }

    @Test
    fun `리뷰를 등록할 수 있다`() {
        // given
        val reviewer = UserFixtureHelper.createDefaultEmailUser(userRepository)
        val question = createDummyQuestion()
        addUserQuestion(reviewer.uid, question.id)
        val questionReview = QuestionReview.create(question.id, reviewer.uid, "comment", 5)
        BDDMockito.willReturn(PublishResponse.builder().build()).given(snsClient).publish(any<PublishRequest>())

        // when
        hubReviewService.register(questionReview)

        // then
        val registeredReview = questionReviewRepository.getMyQuestionReview(question.id, reviewer.uid)
        Assertions.assertThat(registeredReview).isNotNull()
        Assertions.assertThat(registeredReview.reviewerId).isEqualTo(reviewer.uid)
        Assertions.assertThat(registeredReview.questionId).isEqualTo(question.id)
        Assertions.assertThat(registeredReview.comment).isEqualTo(questionReview.comment)
        Assertions.assertThat(registeredReview.rate).isEqualTo(questionReview.rate)

        Assertions.assertThat(events.stream(ReviewEvent::class.java).count()).isEqualTo(1)
    }

    @Test
    fun `리뷰를 수정할 수 있다`() {
        // given
        val reviewer = UserFixtureHelper.createDefaultEmailUser(userRepository)
        val question = createDummyQuestion()
        addUserQuestion(reviewer.uid, question.id)
        val questionReview =
            questionReviewRepository.save(QuestionReview.create(question.id, reviewer.uid, "comment", 5))
        BDDMockito.willReturn(PublishResponse.builder().build()).given(snsClient).publish(any<PublishRequest>())

        // when
        val newComment = "newComment"
        val newRate = 1
        hubReviewService.modify(questionReview.id, reviewer.uid, newComment, newRate)

        // then
        val modifiedReview = questionReviewRepository.getMyQuestionReview(question.id, reviewer.uid)
        Assertions.assertThat(modifiedReview).isNotNull()
        Assertions.assertThat(modifiedReview.comment).isEqualTo(newComment)
        Assertions.assertThat(modifiedReview.rate).isEqualTo(newRate)

        Assertions.assertThat(events.stream(ReviewEvent::class.java).count()).isEqualTo(1)
    }

    @Test
    fun `리뷰를 삭제할 수 있다`() {
        // given
        val reviewer = UserFixtureHelper.createDefaultEmailUser(userRepository)
        val question = createDummyQuestion()
        addUserQuestion(reviewer.uid, question.id)
        val questionReview =
            questionReviewRepository.save(QuestionReview.create(question.id, reviewer.uid, "comment", 5))
        BDDMockito.willReturn(PublishResponse.builder().build()).given(snsClient).publish(any<PublishRequest>())

        // when
        hubReviewService.delete(questionReview.id, reviewer.uid)

        // then
        Assertions.assertThatThrownBy { hubReviewService.getMyQuestionReview(question.id, reviewer.uid) }
            .isInstanceOf(CoreException::class.java)
            .hasFieldOrPropertyWithValue("error", Error.NOT_FOUND)

        Assertions.assertThat(events.stream(ReviewEvent::class.java).count()).isEqualTo(1)
    }

    private fun createDummyQuestion(questionStatus: QuestionStatus = QuestionStatus.Available): Question {
        return QuestionFixtureHelper.createQuestion(
            creatorId = creatorId,
            questionStatus = questionStatus,
            questionRepository = questionRepository
        )
    }

    private fun addUserQuestion(uid: Long, questionId: Long) {
        userQuestionRepository.saveAll(listOf(UserQuestion.create(uid, questionId)))
    }
}