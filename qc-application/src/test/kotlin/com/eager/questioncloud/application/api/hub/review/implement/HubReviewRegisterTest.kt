package com.eager.questioncloud.application.api.hub.review.implement

import com.eager.questioncloud.application.utils.DBCleaner
import com.eager.questioncloud.application.utils.fixture.helper.CreatorFixtureHelper
import com.eager.questioncloud.application.utils.fixture.helper.QuestionFixtureHelper
import com.eager.questioncloud.application.utils.fixture.helper.UserFixtureHelper
import com.eager.questioncloud.core.domain.creator.infrastructure.repository.CreatorRepository
import com.eager.questioncloud.core.domain.question.enums.QuestionStatus
import com.eager.questioncloud.core.domain.question.infrastructure.repository.QuestionRepository
import com.eager.questioncloud.core.domain.question.model.Question
import com.eager.questioncloud.core.domain.review.infrastructure.repository.QuestionReviewRepository
import com.eager.questioncloud.core.domain.review.model.QuestionReview
import com.eager.questioncloud.core.domain.user.infrastructure.repository.UserRepository
import com.eager.questioncloud.core.domain.userquestion.infrastructure.entity.UserQuestionEntity
import com.eager.questioncloud.core.domain.userquestion.infrastructure.repository.UserQuestionJpaRepository
import com.eager.questioncloud.core.domain.userquestion.model.UserQuestion
import com.eager.questioncloud.core.exception.CoreException
import com.eager.questioncloud.core.exception.Error
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors

@SpringBootTest
@ActiveProfiles("test")
class HubReviewRegisterTest(
    @Autowired val userRepository: UserRepository,
    @Autowired val hubReviewRegister: HubReviewRegister,
    @Autowired val questionRepository: QuestionRepository,
    @Autowired val questionReviewRepository: QuestionReviewRepository,
    @Autowired val userQuestionJpaRepository: UserQuestionJpaRepository,
    @Autowired val creatorRepository: CreatorRepository,
    @Autowired val dbCleaner: DBCleaner,
) {
    private var uid: Long = 0

    @BeforeEach
    fun setUp() {
        uid = UserFixtureHelper.createDefaultEmailUser(userRepository).uid
    }

    @AfterEach
    fun tearDown() {
        dbCleaner.cleanUp()
    }

    @Test
    fun `리뷰를 작성할 수 있다`() {
        // given
        val reviewerId = uid
        val question = dummyQuestion()
        saveUserQuestion(reviewerId, question.id)
        val questionReview = QuestionReview.create(question.id, reviewerId, "comment", 5)

        // when
        hubReviewRegister.register(questionReview)

        // then
        val registeredReview = questionReviewRepository.findByQuestionIdAndUserId(question.id, reviewerId)
        Assertions.assertThat(registeredReview).isNotNull
        Assertions.assertThat(registeredReview.comment).isEqualTo(questionReview.comment)
        Assertions.assertThat(registeredReview.rate).isEqualTo(questionReview.rate)
    }

    @Test
    fun `이용 불가능한 문제인경우 리뷰를 작성할 수 없다`() {
        // given
        val reviewerId = uid
        val unavailableQuestion = dummyQuestion(QuestionStatus.UnAvailable)
        saveUserQuestion(reviewerId, unavailableQuestion.id)
        val questionReview = QuestionReview.create(unavailableQuestion.id, reviewerId, "comment", 5)

        // when then
        Assertions.assertThatThrownBy {
            hubReviewRegister.register(questionReview)
        }
            .isInstanceOf(CoreException::class.java)
            .hasFieldOrPropertyWithValue("error", Error.UNAVAILABLE_QUESTION)
    }

    @Test
    fun `미보유 문제는 리뷰를 작성할 수 없다`() {
        // given
        val reviewerId = uid
        val notOwnedQuestion = dummyQuestion()
        val questionReview = QuestionReview.create(notOwnedQuestion.id, reviewerId, "comment", 5)

        // when then
        Assertions.assertThatThrownBy {
            hubReviewRegister.register(questionReview)
        }
            .isInstanceOf(CoreException::class.java)
            .hasFieldOrPropertyWithValue("error", Error.NOT_OWNED_QUESTION)
    }

    @Test
    fun `리뷰는 한번만 작성할 수 있다`() {
        // given
        val reviewerId = uid
        val alreadyWrittenReviewQuestion = dummyQuestion()
        val questionReview = QuestionReview.create(alreadyWrittenReviewQuestion.id, reviewerId, "comment", 5)
        saveUserQuestion(reviewerId, alreadyWrittenReviewQuestion.id)
        questionReviewRepository.save(questionReview)

        // when then
        Assertions.assertThatThrownBy {
            hubReviewRegister.register(questionReview)
        }
            .isInstanceOf(CoreException::class.java)
            .hasFieldOrPropertyWithValue("error", Error.ALREADY_REGISTER_REVIEW)
    }

    @Test
    fun `리뷰 중복 작성 동시성 문제를 방지할 수 있다`() {
        //given
        val reviewerId = uid
        val question = dummyQuestion()
        saveUserQuestion(reviewerId, question.id)
        val questionReview = QuestionReview.create(question.id, reviewerId, "comment", 5)

        //when
        val threadCount = 100
        val executorService = Executors.newFixedThreadPool(threadCount)
        val latch = CountDownLatch(threadCount)

        for (i in 0..<threadCount) {
            executorService.execute {
                try {
                    hubReviewRegister.register(questionReview)
                } catch (ignored: Exception) {
                } finally {
                    latch.countDown()
                }
            }
        }

        latch.await()

        //then
        val reviewCount = questionReviewRepository.countByQuestionId(question.id)
        Assertions.assertThat(reviewCount).isEqualTo(1)
    }

    private fun dummyQuestion(questionStatus: QuestionStatus = QuestionStatus.Available): Question {
        val dummyCreatorUser = UserFixtureHelper.createDefaultEmailUser(userRepository)
        val dummyCreator = CreatorFixtureHelper.createCreator(dummyCreatorUser.uid, creatorRepository)
        return QuestionFixtureHelper.createQuestion(
            creatorId = dummyCreator.id,
            questionStatus = questionStatus,
            questionRepository = questionRepository
        )
    }

    private fun saveUserQuestion(uid: Long, questionId: Long) {
        userQuestionJpaRepository.save(UserQuestionEntity.from(UserQuestion.create(uid, questionId)))
    }
}