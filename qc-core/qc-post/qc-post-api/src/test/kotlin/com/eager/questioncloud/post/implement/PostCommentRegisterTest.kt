package com.eager.questioncloud.post.implement

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.post.repository.PostCommentRepository
import com.eager.questioncloud.question.api.internal.QuestionInformationQueryResult
import com.eager.questioncloud.question.api.internal.QuestionQueryAPI
import com.eager.questioncloud.utils.DBCleaner
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
class PostCommentRegisterTest(
    @Autowired val postCommentRegister: PostCommentRegister,
    @Autowired val postCommentRepository: PostCommentRepository,
    @Autowired val dbCleaner: DBCleaner,
) {
    @MockBean
    lateinit var postPermissionChecker: PostPermissionChecker
    
    @MockBean
    lateinit var questionQueryAPI: QuestionQueryAPI
    
    @AfterEach
    fun tearDown() {
        dbCleaner.cleanUp()
    }
    
    @Test
    fun `일반 사용자가 댓글을 등록할 수 있다`() {
        //given
        val userId = 1L
        val postId = 100L
        val creatorId = 301L
        val questionId = 500L
        val comment = "좋은 문제네요!"
        
        given(postPermissionChecker.hasCommentPermission(userId, postId))
            .willReturn(true)
        
        given(postPermissionChecker.isCreator(userId, questionId))
            .willReturn(false)
        
        val questionInformation = QuestionInformationQueryResult(
            id = questionId,
            creatorId = creatorId,
            title = "테스트 문제",
            subject = "수학",
            parentCategory = "수학",
            childCategory = "대수",
            thumbnail = "thumbnail.jpg",
            questionLevel = "중급",
            price = 1000,
            rate = 4.5
        )
        given(questionQueryAPI.getQuestionInformation(postId))
            .willReturn(questionInformation)
        
        //when
        val result = postCommentRegister.register(postId, userId, comment)
        
        //then
        Assertions.assertThat(result).isNotNull
        Assertions.assertThat(result.postId).isEqualTo(postId)
        Assertions.assertThat(result.writerId).isEqualTo(userId)
        Assertions.assertThat(result.comment).isEqualTo(comment)
        Assertions.assertThat(result.isCreator).isFalse()
        
        val commentCount = postCommentRepository.count(postId)
        Assertions.assertThat(commentCount).isEqualTo(1)
    }
    
    @Test
    fun `크리에이터가 댓글을 등록할 수 있다`() {
        //given
        val userId = 2L
        val postId = 200L
        val creatorId = 302L
        val questionId = 600L
        val comment = "문제에 대한 부가 설명입니다."
        
        given(postPermissionChecker.hasCommentPermission(userId, postId))
            .willReturn(true)
        
        given(postPermissionChecker.isCreator(userId, questionId))
            .willReturn(true)
        
        val questionInformation = QuestionInformationQueryResult(
            id = questionId,
            creatorId = creatorId,
            title = "테스트 문제",
            subject = "수학",
            parentCategory = "수학",
            childCategory = "대수",
            thumbnail = "thumbnail.jpg",
            questionLevel = "중급",
            price = 1000,
            rate = 4.5
        )
        given(questionQueryAPI.getQuestionInformation(postId))
            .willReturn(questionInformation)
        
        //when
        val result = postCommentRegister.register(postId, userId, comment)
        
        //then
        Assertions.assertThat(result).isNotNull
        Assertions.assertThat(result.postId).isEqualTo(postId)
        Assertions.assertThat(result.writerId).isEqualTo(userId)
        Assertions.assertThat(result.comment).isEqualTo(comment)
        Assertions.assertThat(result.isCreator).isTrue()
        
        val commentCount = postCommentRepository.count(postId)
        Assertions.assertThat(commentCount).isEqualTo(1)
    }
    
    @Test
    fun `댓글 작성 권한이 없으면 예외가 발생한다`() {
        //given
        val userId = 3L
        val postId = 300L
        val comment = "권한 없이 작성하려는 댓글"
        
        given(postPermissionChecker.hasCommentPermission(userId, postId))
            .willReturn(false)
        
        //when & then
        assertThrows<CoreException> {
            postCommentRegister.register(postId, userId, comment)
        }
        
        val commentCount = postCommentRepository.count(postId)
        Assertions.assertThat(commentCount).isEqualTo(0)
    }
}
