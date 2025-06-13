package com.eager.questioncloud.application.api.hub.review.implement

import com.eager.questioncloud.application.utils.DBCleaner
import com.eager.questioncloud.application.utils.fixture.helper.UserFixtureHelper
import com.eager.questioncloud.core.domain.review.infrastructure.repository.QuestionReviewRepository
import com.eager.questioncloud.core.domain.review.model.QuestionReview
import com.eager.questioncloud.core.domain.user.infrastructure.repository.UserRepository
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
class HubReviewUpdaterTest(
    @Autowired val userRepository: UserRepository,
    @Autowired val questionReviewRepository: QuestionReviewRepository,
    @Autowired val hubReviewUpdater: HubReviewUpdater,
    @Autowired val dbCleaner: DBCleaner,
) {
    @AfterEach
    fun tearDown() {
        dbCleaner.cleanUp()
    }

    @Test
    fun `작성한 리뷰를 수정할 수 있다`() {
        // given
        val reviewer = UserFixtureHelper.createDefaultEmailUser(userRepository)
        val dummyQuestionId = 1L
        val review = questionReviewRepository.save(QuestionReview.create(dummyQuestionId, reviewer.uid, "comment", 5))

        val newComment = "hello world"
        val newRate = 2

        // when
        hubReviewUpdater.modify(review.id, reviewer.uid, newComment, newRate)

        // then
        val updatedReview = questionReviewRepository.findByQuestionIdAndUserId(dummyQuestionId, reviewer.uid)
        Assertions.assertThat(updatedReview).isNotNull
        Assertions.assertThat(updatedReview.comment).isEqualTo(newComment)
        Assertions.assertThat(updatedReview.rate).isEqualTo(newRate)
    }
}