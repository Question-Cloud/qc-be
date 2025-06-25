package com.eager.questioncloud.review.implement

import com.eager.questioncloud.review.domain.QuestionReview
import com.eager.questioncloud.review.infrastructure.repository.QuestionReviewRepository
import com.eager.questioncloud.utils.DBCleaner
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
class StoreReviewRemoverTest(
    @Autowired val storeReviewRemover: StoreReviewRemover,
    @Autowired val questionReviewRepository: QuestionReviewRepository,
    @Autowired val dbCleaner: DBCleaner,
) {
    private val questionId = 1L
    private val userId = 100L
    private val otherUserId = 200L

    private lateinit var savedReview: QuestionReview

    @BeforeEach
    fun setUp() {
        val questionReview = QuestionReview.create(questionId, userId, "좋은 문제입니다", 5)
        savedReview = questionReviewRepository.save(questionReview)

        questionReviewRepository.save(
            QuestionReview.create(questionId, otherUserId, "다른 사용자 리뷰", 4)
        )
    }

    @AfterEach
    fun tearDown() {
        dbCleaner.cleanUp()
    }

    @Test
    fun `리뷰를 삭제할 수 있다`() {
        //when
        val result = storeReviewRemover.delete(savedReview.id, userId)

        //then
        Assertions.assertThat(result.first).isEqualTo(questionId)
        Assertions.assertThat(result.second).isEqualTo(5)
        
        Assertions.assertThat(questionReviewRepository.isWritten(userId, savedReview.questionId)).isFalse()
    }
}
