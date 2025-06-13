package com.eager.questioncloud.application.api.hub.review.implement

import com.eager.questioncloud.application.utils.DBCleaner
import com.eager.questioncloud.application.utils.fixture.helper.UserFixtureHelper
import com.eager.questioncloud.core.domain.review.infrastructure.repository.QuestionReviewRepository
import com.eager.questioncloud.core.domain.review.model.QuestionReview
import com.eager.questioncloud.core.domain.user.infrastructure.repository.UserRepository
import com.eager.questioncloud.core.exception.CoreException
import com.eager.questioncloud.core.exception.Error
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
class HubReviewRemoverTest(
    @Autowired val userRepository: UserRepository,
    @Autowired val questionReviewRepository: QuestionReviewRepository,
    @Autowired val hubReviewRemover: HubReviewRemover,
    @Autowired val dbCleaner: DBCleaner,
) {
    @AfterEach
    fun tearDown() {
        dbCleaner.cleanUp()
    }

    @Test
    fun `작성한 리뷰를 삭제할 수 있다`() {
        // given
        val dummyReviewer = UserFixtureHelper.createDefaultEmailUser(userRepository)
        val dummyQuestionId = 1L
        val questionReview =
            questionReviewRepository.save(QuestionReview.create(dummyQuestionId, dummyReviewer.uid, "comment", 5))


        // when
        hubReviewRemover.delete(questionReview.id, dummyReviewer.uid)

        // then
        Assertions.assertThatThrownBy {
            questionReviewRepository.findByQuestionIdAndUserId(dummyQuestionId, dummyReviewer.uid)
        }
            .isInstanceOf(CoreException::class.java)
            .hasFieldOrPropertyWithValue("error", Error.NOT_FOUND)
    }
}