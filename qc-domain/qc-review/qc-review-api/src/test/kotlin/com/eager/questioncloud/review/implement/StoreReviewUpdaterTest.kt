package com.eager.questioncloud.review.implement

import com.eager.questioncloud.review.domain.QuestionReview
import com.eager.questioncloud.review.infrastructure.repository.QuestionReviewRepository
import com.eager.questioncloud.utils.DBCleaner
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
class StoreReviewUpdaterTest(
    @Autowired val storeReviewUpdater: StoreReviewUpdater,
    @Autowired val questionReviewRepository: QuestionReviewRepository,
    @Autowired val dbCleaner: DBCleaner,
) {
    @AfterEach
    fun tearDown() {
        dbCleaner.cleanUp()
    }

    @Test
    fun `리뷰를 수정할 수 있다`() {
        val questionId = 1L
        val userId = 100L
        val otherUserId = 200L

        val questionReview = QuestionReview.create(questionId, userId, "원래 코멘트", 3)
        val savedReview = questionReviewRepository.save(questionReview)

        questionReviewRepository.save(
            QuestionReview.create(questionId, otherUserId, "다른 사용자 리뷰", 4)
        )

        //when
        val result = storeReviewUpdater.modify(savedReview.id, userId, "수정된 코멘트", 5)

        //then
        Assertions.assertThat(result.first).isEqualTo(questionId)
        Assertions.assertThat(result.second).isEqualTo(2)

        val updatedReview = questionReviewRepository.findByIdAndUserId(savedReview.id, userId)
        Assertions.assertThat(updatedReview.comment).isEqualTo("수정된 코멘트")
        Assertions.assertThat(updatedReview.rate).isEqualTo(5)
    }
}
