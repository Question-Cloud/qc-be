package com.eager.questioncloud.application.api.review.implement

import com.eager.questioncloud.application.api.hub.review.implement.HubReviewRegister
import com.eager.questioncloud.application.utils.fixture.helper.CreatorFixtureHelper
import com.eager.questioncloud.application.utils.fixture.helper.QuestionFixtureHelper
import com.eager.questioncloud.application.utils.fixture.helper.UserFixtureHelper
import com.eager.questioncloud.core.domain.creator.infrastructure.repository.CreatorRepository
import com.eager.questioncloud.core.domain.question.infrastructure.repository.QuestionRepository
import com.eager.questioncloud.core.domain.review.infrastructure.repository.QuestionReviewJpaRepository
import com.eager.questioncloud.core.domain.review.model.QuestionReview.Companion.create
import com.eager.questioncloud.core.domain.user.infrastructure.repository.UserRepository
import com.eager.questioncloud.core.domain.userquestion.infrastructure.entity.UserQuestionEntity
import com.eager.questioncloud.core.domain.userquestion.infrastructure.repository.UserQuestionJpaRepository
import com.eager.questioncloud.core.domain.userquestion.model.UserQuestion
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
internal class HubReviewRegisterTest(
    @Autowired val userRepository: UserRepository,
    @Autowired val hubReviewRegister: HubReviewRegister,
    @Autowired val questionRepository: QuestionRepository,
    @Autowired val questionReviewJpaRepository: QuestionReviewJpaRepository,
    @Autowired val userQuestionJpaRepository: UserQuestionJpaRepository,
    @Autowired val creatorRepository: CreatorRepository,
) {
    private var uid: Long = 0
    private var creatorId: Long = 0

    @BeforeEach
    fun setUp() {
        uid = UserFixtureHelper.createDefaultEmailUser(userRepository).uid
        creatorId = CreatorFixtureHelper.createCreator(uid, creatorRepository).id
    }

    @AfterEach
    fun tearDown() {
        questionRepository.deleteAllInBatch()
        questionReviewJpaRepository.deleteAllInBatch()
        userQuestionJpaRepository.deleteAllInBatch()
        creatorRepository.deleteAllInBatch()
    }

    @Test
    fun `리뷰 중복 작성 동시성 문제를 방지할 수 있다`() {
        //given
        val reviewerId = uid
        val question =
            QuestionFixtureHelper.createQuestion(creatorId = creatorId, questionRepository = questionRepository)
        userQuestionJpaRepository.save(UserQuestionEntity.from(UserQuestion.create(reviewerId, question.id)))
        val questionReview = create(question.id, reviewerId, "comment", 5)

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
        val reviewCount = questionReviewJpaRepository.count().toInt()
        Assertions.assertThat(reviewCount).isEqualTo(1)
    }
}