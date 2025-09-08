package com.eager.questioncloud.post.service

import com.eager.questioncloud.common.pagination.PagingInformation
import com.eager.questioncloud.post.domain.PostComment
import com.eager.questioncloud.post.implement.PostPermissionChecker
import com.eager.questioncloud.post.repository.PostCommentRepository
import com.eager.questioncloud.question.api.internal.QuestionInformationQueryResult
import com.eager.questioncloud.question.api.internal.QuestionQueryAPI
import com.eager.questioncloud.user.api.internal.UserQueryAPI
import com.eager.questioncloud.user.api.internal.UserQueryData
import com.eager.questioncloud.utils.DBCleaner
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
class PostCommentServiceTest(
    @Autowired val postCommentService: PostCommentService,
    @Autowired val postCommentRepository: PostCommentRepository,
    @Autowired val dbCleaner: DBCleaner,
) {
    @MockBean
    lateinit var postPermissionChecker: PostPermissionChecker
    
    @MockBean
    lateinit var questionQueryAPI: QuestionQueryAPI
    
    @MockBean
    lateinit var userQueryAPI: UserQueryAPI
    
    @AfterEach
    fun tearDown() {
        dbCleaner.cleanUp()
    }
    
    @Test
    fun `댓글을 등록할 수 있다`() {
        //given
        val postId = 100L
        val userId = 200L
        val questionId = 300L
        val creatorId = 400L
        val comment = "테스트 댓글입니다"
        
        given(postPermissionChecker.hasCommentPermission(userId, postId))
            .willReturn(true)
        
        given(questionQueryAPI.getQuestionInformation(postId))
            .willReturn(createQuestionInformation(questionId, creatorId))
        
        given(postPermissionChecker.isCreator(userId, questionId))
            .willReturn(false)
        
        //when
        postCommentService.addPostComment(postId, userId, comment)
        
        //then
        val commentCount = postCommentRepository.count(postId)
        Assertions.assertThat(commentCount).isEqualTo(1)
    }
    
    @Test
    fun `댓글을 수정할 수 있다`() {
        //given
        val postId = 101L
        val userId = 201L
        val originalComment = "원본 댓글"
        val modifiedComment = "수정된 댓글"
        
        val postComment = PostComment.create(postId, userId, originalComment, false)
        val savedComment = postCommentRepository.save(postComment)
        
        //when
        postCommentService.modifyPostComment(savedComment.id, userId, modifiedComment)
        
        //then
        val updatedComment = postCommentRepository.findByIdAndWriterId(savedComment.id, userId)
        Assertions.assertThat(updatedComment.comment).isEqualTo(modifiedComment)
    }
    
    @Test
    fun `댓글을 삭제할 수 있다`() {
        //given
        val postId = 102L
        val userId = 202L
        val comment = "삭제될 댓글"
        
        val postComment = PostComment.create(postId, userId, comment, false)
        val savedComment = postCommentRepository.save(postComment)
        
        //when
        postCommentService.deletePostComment(savedComment.id, userId)
        
        //then
        val commentCount = postCommentRepository.count(postId)
        Assertions.assertThat(commentCount).isEqualTo(0)
    }
    
    @Test
    fun `댓글 목록을 조회할 수 있다`() {
        //given
        val postId = 103L
        val userId = 203L
        val writerId1 = 301L
        val writerId2 = 302L
        
        val comment1 = PostComment.create(postId, writerId1, "첫 번째 댓글", false)
        val comment2 = PostComment.create(postId, writerId2, "두 번째 댓글", false)
        postCommentRepository.save(comment1)
        postCommentRepository.save(comment2)
        
        given(postPermissionChecker.hasCommentPermission(userId, postId))
            .willReturn(true)
        
        val userData1 = UserQueryData(writerId1, "작성자1", "profile1.jpg", "user1@test.com")
        val userData2 = UserQueryData(writerId2, "작성자2", "profile2.jpg", "user2@test.com")
        given(userQueryAPI.getUsers(listOf(writerId1, writerId2)))
            .willReturn(listOf(userData1, userData2))
        
        val pagingInformation = PagingInformation(0, 10)
        
        //when
        val result = postCommentService.getPostCommentDetails(postId, userId, pagingInformation)
        
        //then
        Assertions.assertThat(result).hasSize(2)
        
        val commentDetail1 = result.find { it.comment == "첫 번째 댓글" }
        Assertions.assertThat(commentDetail1).isNotNull
        Assertions.assertThat(commentDetail1!!.writerName).isEqualTo("작성자1")
        
        val commentDetail2 = result.find { it.comment == "두 번째 댓글" }
        Assertions.assertThat(commentDetail2).isNotNull
        Assertions.assertThat(commentDetail2!!.writerName).isEqualTo("작성자2")
    }
    
    @Test
    fun `댓글 개수를 조회할 수 있다`() {
        //given
        val postId = 104L
        val userId = 204L
        
        val comment1 = PostComment.create(postId, userId, "댓글1", false)
        val comment2 = PostComment.create(postId, userId, "댓글2", false)
        val comment3 = PostComment.create(postId, userId, "댓글3", false)
        postCommentRepository.save(comment1)
        postCommentRepository.save(comment2)
        postCommentRepository.save(comment3)
        
        //when
        val result = postCommentService.count(postId)
        
        //then
        Assertions.assertThat(result).isEqualTo(3)
    }
    
    private fun createQuestionInformation(questionId: Long, creatorId: Long): QuestionInformationQueryResult {
        return QuestionInformationQueryResult(
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
    }
}
