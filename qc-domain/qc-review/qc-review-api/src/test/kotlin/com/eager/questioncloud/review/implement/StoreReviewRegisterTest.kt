package com.eager.questioncloud.review.implement

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import com.eager.questioncloud.question.api.internal.QuestionQueryAPI
import com.eager.questioncloud.review.domain.QuestionReview
import com.eager.questioncloud.review.infrastructure.repository.QuestionReviewRepository
import com.eager.questioncloud.utils.DBCleaner
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ActiveProfiles
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors

@SpringBootTest
@ActiveProfiles("test")
class StoreReviewRegisterTest(
    @Autowired val storeReviewRegister: StoreReviewRegister,
    @Autowired val questionReviewRepository: QuestionReviewRepository,
    @Autowired val dbCleaner: DBCleaner,
) {
    @MockBean
    lateinit var questionQueryAPI: QuestionQueryAPI

    private val questionId = 1L
    private val userId = 100L

    @AfterEach
    fun tearDown() {
        dbCleaner.cleanUp()
    }

    @Test
    fun `리뷰를 등록할 수 있다`() {
        //given
        val questionReview = QuestionReview.create(questionId, userId, "좋은 문제입니다", 5)

        given(questionQueryAPI.isAvailable(questionId)).willReturn(true)
        given(questionQueryAPI.isOwned(userId, questionId)).willReturn(true)

        //when
        storeReviewRegister.register(questionReview)

        //then
        val isWritten = questionReviewRepository.isWritten(userId, questionId)
        Assertions.assertThat(isWritten).isTrue()

        val savedReview = questionReviewRepository.findByQuestionIdAndUserId(questionId, userId)
        Assertions.assertThat(savedReview.comment).isEqualTo("좋은 문제입니다")
        Assertions.assertThat(savedReview.rate).isEqualTo(5)
    }

    @Test
    fun `사용할 수 없는 문제에는 리뷰를 등록할 수 없다`() {
        //given
        val questionReview = QuestionReview.create(questionId, userId, "좋은 문제입니다", 5)

        given(questionQueryAPI.isAvailable(questionId)).willReturn(false)

        //when then
        Assertions.assertThatThrownBy {
            storeReviewRegister.register(questionReview)
        }
            .isInstanceOf(CoreException::class.java)
            .hasFieldOrPropertyWithValue("error", Error.UNAVAILABLE_QUESTION)
    }

    @Test
    fun `소유하지 않은 문제에는 리뷰를 등록할 수 없다`() {
        //given
        val questionReview = QuestionReview.create(questionId, userId, "좋은 문제입니다", 5)

        given(questionQueryAPI.isAvailable(questionId)).willReturn(true)
        given(questionQueryAPI.isOwned(userId, questionId)).willReturn(false)

        //when then
        Assertions.assertThatThrownBy {
            storeReviewRegister.register(questionReview)
        }
            .isInstanceOf(CoreException::class.java)
            .hasFieldOrPropertyWithValue("error", Error.NOT_OWNED_QUESTION)
    }

    @Test
    fun `이미 리뷰를 작성한 문제에는 중복 등록할 수 없다`() {
        //given
        val questionReview = QuestionReview.create(questionId, userId, "좋은 문제입니다", 5)

        given(questionQueryAPI.isAvailable(questionId)).willReturn(true)
        given(questionQueryAPI.isOwned(userId, questionId)).willReturn(true)

        storeReviewRegister.register(questionReview)

        val duplicateReview = QuestionReview.create(questionId, userId, "다른 리뷰", 4)

        //when then
        Assertions.assertThatThrownBy {
            storeReviewRegister.register(duplicateReview)
        }
            .isInstanceOf(CoreException::class.java)
            .hasFieldOrPropertyWithValue("error", Error.ALREADY_REGISTER_REVIEW)
    }

    @Test
    fun `리뷰 중복 작성 동시성 문제를 방지할 수 있다`() {
        //given
        val concurrencyQuestionId = 999L
        val questionReview = QuestionReview.create(concurrencyQuestionId, userId, "동시성 테스트", 5)

        given(questionQueryAPI.isAvailable(concurrencyQuestionId)).willReturn(true)
        given(questionQueryAPI.isOwned(userId, concurrencyQuestionId)).willReturn(true)

        //when
        val threadCount = 100
        val executorService = Executors.newFixedThreadPool(threadCount)
        val latch = CountDownLatch(threadCount)

        for (i in 0..<threadCount) {
            executorService.execute {
                try {
                    storeReviewRegister.register(questionReview)
                } catch (ignored: Exception) {
                } finally {
                    latch.countDown()
                }
            }
        }

        latch.await()

        //then
        val reviewCount = questionReviewRepository.countByQuestionId(concurrencyQuestionId)
        Assertions.assertThat(reviewCount).isEqualTo(1)
    }
}
