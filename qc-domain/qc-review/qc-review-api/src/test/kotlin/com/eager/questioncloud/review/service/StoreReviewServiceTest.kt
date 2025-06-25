package com.eager.questioncloud.review.service

import com.eager.questioncloud.common.pagination.PagingInformation
import com.eager.questioncloud.question.api.internal.QuestionQueryAPI
import com.eager.questioncloud.review.domain.QuestionReview
import com.eager.questioncloud.review.implement.StoreReviewEventProcessor
import com.eager.questioncloud.review.infrastructure.repository.QuestionReviewRepository
import com.eager.questioncloud.user.api.internal.UserQueryAPI
import com.eager.questioncloud.user.api.internal.UserQueryData
import com.eager.questioncloud.utils.DBCleaner
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.given
import org.mockito.kotlin.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
class StoreReviewServiceTest(
    @Autowired val storeReviewService: StoreReviewService,
    @Autowired val questionReviewRepository: QuestionReviewRepository,
    @Autowired val dbCleaner: DBCleaner,
) {
    @MockBean
    lateinit var userQueryAPI: UserQueryAPI

    @MockBean
    lateinit var questionQueryAPI: QuestionQueryAPI

    @MockBean
    lateinit var storeReviewEventProcessor: StoreReviewEventProcessor

    @AfterEach
    fun tearDown() {
        dbCleaner.cleanUp()
    }

    @Test
    fun `리뷰 개수를 조회할 수 있다`() {
        //given
        val questionId = 1L
        val userId = 100L
        val questionReview = QuestionReview.create(questionId, userId, "좋은 문제입니다", 5)
        questionReviewRepository.save(questionReview)

        //when
        val count = storeReviewService.count(questionId)

        //then
        Assertions.assertThat(count).isEqualTo(1)
    }

    @Test
    fun `리뷰 상세 정보를 조회할 수 있다`() {
        //given
        val questionId = 1L
        val userId = 100L
        val questionReview = QuestionReview.create(questionId, userId, "좋은 문제입니다", 5)
        questionReviewRepository.save(questionReview)

        val pagingInformation = PagingInformation(0, 10)
        val userQueryData = UserQueryData(userId, "리뷰어", "profile.jpg", "reviewer@test.com")

        given(userQueryAPI.getUsers(listOf(userId))).willReturn(listOf(userQueryData))

        //when
        val reviewDetails = storeReviewService.getReviewDetails(questionId, userId, pagingInformation)

        //then
        Assertions.assertThat(reviewDetails).hasSize(1)
        Assertions.assertThat(reviewDetails[0].reviewerName).isEqualTo("리뷰어")
        Assertions.assertThat(reviewDetails[0].comment).isEqualTo("좋은 문제입니다")
        Assertions.assertThat(reviewDetails[0].rate).isEqualTo(5)
        Assertions.assertThat(reviewDetails[0].isWriter).isTrue()
    }

    @Test
    fun `내 리뷰를 조회할 수 있다`() {
        //given
        val questionId = 1L
        val userId = 100L
        val questionReview = QuestionReview.create(questionId, userId, "좋은 문제입니다", 5)
        questionReviewRepository.save(questionReview)

        //when
        val myReview = storeReviewService.getMyReview(questionId, userId)

        //then
        Assertions.assertThat(myReview.comment).isEqualTo("좋은 문제입니다")
        Assertions.assertThat(myReview.rate).isEqualTo(5)
    }

    @Test
    fun `리뷰를 등록할 수 있다`() {
        //given
        val questionId = 1L
        val userId = 100L
        val newReview = QuestionReview.create(questionId, userId, "새로운 리뷰", 4)

        given(questionQueryAPI.isAvailable(questionId)).willReturn(true)
        given(questionQueryAPI.isOwned(userId, questionId)).willReturn(true)

        //when
        storeReviewService.register(newReview)

        //then
        val isWritten = questionReviewRepository.isWritten(userId, questionId)
        Assertions.assertThat(isWritten).isTrue()

        // 이벤트 처리가 호출되었는지 확인
        verify(storeReviewEventProcessor).saveEventLog(any())
    }

    @Test
    fun `리뷰를 수정할 수 있다`() {
        //given
        val questionId = 1L
        val userId = 100L
        val questionReview = QuestionReview.create(questionId, userId, "원래 리뷰", 3)
        val savedReview = questionReviewRepository.save(questionReview)

        //when
        storeReviewService.modify(savedReview.id, userId, "수정된 리뷰", 5)

        //then
        val updatedReview = questionReviewRepository.findByIdAndUserId(savedReview.id, userId)
        Assertions.assertThat(updatedReview.comment).isEqualTo("수정된 리뷰")
        Assertions.assertThat(updatedReview.rate).isEqualTo(5)

        // 이벤트 처리가 호출되었는지 확인
        verify(storeReviewEventProcessor).saveEventLog(any())
    }

    @Test
    fun `리뷰를 삭제할 수 있다`() {
        //given
        val questionId = 1L
        val userId = 100L
        val questionReview = QuestionReview.create(questionId, userId, "삭제할 리뷰", 4)
        val savedReview = questionReviewRepository.save(questionReview)

        //when
        storeReviewService.delete(savedReview.id, userId)

        //then
        Assertions.assertThat(questionReviewRepository.isWritten(userId, questionId)).isFalse()

        // 이벤트 처리가 호출되었는지 확인
        verify(storeReviewEventProcessor).saveEventLog(any())
    }

    @Test
    fun `여러 리뷰 작업을 연속으로 수행할 수 있다`() {
        //given
        val questionId1 = 1L
        val questionId2 = 2L
        val questionId3 = 3L
        val userId = 100L

        // 수정할 기존 리뷰 생성
        val existingReview = QuestionReview.create(questionId1, userId, "기존 리뷰", 3)
        val savedReview = questionReviewRepository.save(existingReview)

        given(questionQueryAPI.isAvailable(questionId2)).willReturn(true)
        given(questionQueryAPI.isAvailable(questionId3)).willReturn(true)
        given(questionQueryAPI.isOwned(userId, questionId2)).willReturn(true)
        given(questionQueryAPI.isOwned(userId, questionId3)).willReturn(true)

        //when
        // 새 리뷰 등록
        val newReview1 = QuestionReview.create(questionId2, userId, "리뷰2", 4)
        storeReviewService.register(newReview1)

        val newReview2 = QuestionReview.create(questionId3, userId, "리뷰3", 2)
        storeReviewService.register(newReview2)

        // 기존 리뷰 수정
        storeReviewService.modify(savedReview.id, userId, "수정된 리뷰", 1)

        //then
        Assertions.assertThat(storeReviewService.count(questionId1)).isEqualTo(1)
        Assertions.assertThat(storeReviewService.count(questionId2)).isEqualTo(1)
        Assertions.assertThat(storeReviewService.count(questionId3)).isEqualTo(1)

        val myReview1 = storeReviewService.getMyReview(questionId1, userId)
        val myReview2 = storeReviewService.getMyReview(questionId2, userId)
        val myReview3 = storeReviewService.getMyReview(questionId3, userId)

        Assertions.assertThat(myReview1.comment).isEqualTo("수정된 리뷰")
        Assertions.assertThat(myReview1.rate).isEqualTo(1)

        Assertions.assertThat(myReview2.comment).isEqualTo("리뷰2")
        Assertions.assertThat(myReview2.rate).isEqualTo(4)

        Assertions.assertThat(myReview3.comment).isEqualTo("리뷰3")
        Assertions.assertThat(myReview3.rate).isEqualTo(2)
    }
}
