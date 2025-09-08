package com.eager.questioncloud.review.implement

import com.eager.questioncloud.common.pagination.PagingInformation
import com.eager.questioncloud.review.domain.QuestionReview
import com.eager.questioncloud.review.repository.QuestionReviewRepository
import com.eager.questioncloud.user.api.internal.UserQueryAPI
import com.eager.questioncloud.user.api.internal.UserQueryData
import com.eager.questioncloud.utils.DBCleaner
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
class StoreReviewReaderTest(
    @Autowired val storeReviewReader: StoreReviewReader,
    @Autowired val questionReviewRepository: QuestionReviewRepository,
    @Autowired val dbCleaner: DBCleaner,
) {
    @MockBean
    lateinit var userQueryAPI: UserQueryAPI
    
    private val questionId = 1L
    private val userId = 100L
    private val reviewerId1 = 101L
    private val reviewerId2 = 102L
    
    @BeforeEach
    fun setUp() {
        questionReviewRepository.save(
            QuestionReview.create(questionId, reviewerId1, "좋은 문제입니다", 5)
        )
        questionReviewRepository.save(
            QuestionReview.create(questionId, reviewerId2, "도움이 되었어요", 4)
        )
        questionReviewRepository.save(
            QuestionReview.create(questionId, userId, "내가 작성한 리뷰", 5)
        )
    }
    
    @AfterEach
    fun tearDown() {
        dbCleaner.cleanUp()
    }
    
    @Test
    fun `문제의 리뷰 개수를 조회할 수 있다`() {
        //when
        val count = storeReviewReader.count(questionId)
        
        //then
        Assertions.assertThat(count).isEqualTo(3)
    }
    
    @Test
    fun `문제의 리뷰 상세 정보를 조회할 수 있다`() {
        //given
        val pagingInformation = PagingInformation(0, 10)
        
        val userQueryData1 = UserQueryData(reviewerId1, "리뷰어1", "profile1.jpg", "reviewer1@test.com")
        val userQueryData2 = UserQueryData(reviewerId2, "리뷰어2", "profile2.jpg", "reviewer2@test.com")
        val userQueryData3 = UserQueryData(userId, "나", "my_profile.jpg", "me@test.com")
        
        given(userQueryAPI.getUsers(listOf(reviewerId1, reviewerId2, userId)))
            .willReturn(listOf(userQueryData1, userQueryData2, userQueryData3))
        
        //when
        val reviewDetails = storeReviewReader.getQuestionReviewDetails(questionId, userId, pagingInformation)
        
        //then
        Assertions.assertThat(reviewDetails).hasSize(3)
        
        val myReview = reviewDetails.find { it.isWriter }
        Assertions.assertThat(myReview).isNotNull
        Assertions.assertThat(myReview!!.reviewerName).isEqualTo("나")
        Assertions.assertThat(myReview.comment).isEqualTo("내가 작성한 리뷰")
        Assertions.assertThat(myReview.rate).isEqualTo(5)
        Assertions.assertThat(myReview.isWriter).isTrue()
        
        val otherReviews = reviewDetails.filter { !it.isWriter }
        Assertions.assertThat(otherReviews).hasSize(2)
        
        val reviewer1Review = otherReviews.find { it.reviewerName == "리뷰어1" }
        Assertions.assertThat(reviewer1Review).isNotNull
        Assertions.assertThat(reviewer1Review!!.comment).isEqualTo("좋은 문제입니다")
        Assertions.assertThat(reviewer1Review.rate).isEqualTo(5)
        Assertions.assertThat(reviewer1Review.isWriter).isFalse()
    }
    
    @Test
    fun `내가 작성한 리뷰를 조회할 수 있다`() {
        //when
        val myReview = storeReviewReader.getMyQuestionReview(questionId, userId)
        
        //then
        Assertions.assertThat(myReview).isNotNull
        Assertions.assertThat(myReview.comment).isEqualTo("내가 작성한 리뷰")
        Assertions.assertThat(myReview.rate).isEqualTo(5)
    }
    
    @Test
    fun `페이징이 적용된다`() {
        //given
        val pagingInformation = PagingInformation(0, 2)
        
        val userQueryData1 = UserQueryData(reviewerId1, "리뷰어1", "profile1.jpg", "reviewer1@test.com")
        val userQueryData2 = UserQueryData(reviewerId2, "리뷰어2", "profile2.jpg", "reviewer2@test.com")
        
        given(userQueryAPI.getUsers(listOf(reviewerId1, reviewerId2)))
            .willReturn(listOf(userQueryData1, userQueryData2))
        
        //when
        val reviewDetails = storeReviewReader.getQuestionReviewDetails(questionId, userId, pagingInformation)
        
        //then
        Assertions.assertThat(reviewDetails).hasSize(2)
    }
}
