package com.eager.questioncloud.post.implement

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.pagination.PagingInformation
import com.eager.questioncloud.post.domain.PostComment
import com.eager.questioncloud.post.repository.PostCommentRepository
import com.eager.questioncloud.user.api.internal.UserQueryAPI
import com.eager.questioncloud.user.api.internal.UserQueryData
import com.eager.questioncloud.utils.DBCleaner
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.any
import org.mockito.kotlin.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
class PostCommentDetailReaderTest(
    @Autowired val postCommentDetailReader: PostCommentDetailReader,
    @Autowired val postCommentRepository: PostCommentRepository,
    @Autowired val dbCleaner: DBCleaner,
) {
    @MockBean
    lateinit var postPermissionChecker: PostPermissionChecker
    
    @MockBean
    lateinit var userQueryAPI: UserQueryAPI
    
    @AfterEach
    fun tearDown() {
        dbCleaner.cleanUp()
    }
    
    @Test
    fun `댓글 목록을 조회할 수 있다`() {
        //given
        val userId = 1L
        val postId = 100L
        val writerId1 = 201L
        val writerId2 = 202L
        val creatorId = 301L
        
        val comment1 = createPostComment(postId, writerId1, "첫 번째 댓글입니다", false)
        val comment2 = createPostComment(postId, creatorId, "크리에이터 댓글입니다", true)
        val comment3 = createPostComment(postId, writerId2, "두 번째 댓글입니다", false)
        
        given(postPermissionChecker.hasCommentPermission(userId, postId))
            .willReturn(true)
        
        val userQueryData1 = UserQueryData(writerId1, "사용자1", "profile1.jpg", "user1@test.com")
        val userQueryData2 = UserQueryData(creatorId, "크리에이터", "creator.jpg", "creator@test.com")
        val userQueryData3 = UserQueryData(writerId2, "사용자2", "profile2.jpg", "user2@test.com")
        
        given(userQueryAPI.getUsers(any()))
            .willReturn(listOf(userQueryData1, userQueryData2, userQueryData3))
        
        val pagingInformation = PagingInformation(0, 10)
        
        //when
        val result = postCommentDetailReader.getPostCommentDetails(userId, postId, pagingInformation)
        
        //then
        Assertions.assertThat(result).hasSize(3)
        
        val commentDetail1 = result.find { it.id == comment1.id }
        Assertions.assertThat(commentDetail1).isNotNull
        Assertions.assertThat(commentDetail1!!.writerName).isEqualTo("사용자1")
        Assertions.assertThat(commentDetail1.profileImage).isEqualTo("profile1.jpg")
        Assertions.assertThat(commentDetail1.comment).isEqualTo("첫 번째 댓글입니다")
        Assertions.assertThat(commentDetail1.isCreator).isFalse()
        Assertions.assertThat(commentDetail1.isWriter).isFalse()
        
        val commentDetail2 = result.find { it.id == comment2.id }
        Assertions.assertThat(commentDetail2).isNotNull
        Assertions.assertThat(commentDetail2!!.writerName).isEqualTo("크리에이터")
        Assertions.assertThat(commentDetail2.profileImage).isEqualTo("creator.jpg")
        Assertions.assertThat(commentDetail2.comment).isEqualTo("크리에이터 댓글입니다")
        Assertions.assertThat(commentDetail2.isCreator).isTrue()
        Assertions.assertThat(commentDetail2.isWriter).isFalse()
        
        val commentDetail3 = result.find { it.id == comment3.id }
        Assertions.assertThat(commentDetail3).isNotNull
        Assertions.assertThat(commentDetail3!!.writerName).isEqualTo("사용자2")
        Assertions.assertThat(commentDetail3.comment).isEqualTo("두 번째 댓글입니다")
        Assertions.assertThat(commentDetail3.isCreator).isFalse()
        Assertions.assertThat(commentDetail3.isWriter).isFalse()
    }
    
    @Test
    fun `자신이 작성한 댓글을 조회할 수 있다`() {
        //given
        val userId = 2L
        val postId = 200L
        val writerId1 = 301L
        
        val myComment = createPostComment(postId, userId, "내가 작성한 댓글", false)
        val otherComment = createPostComment(postId, writerId1, "다른 사람 댓글", false)
        
        given(postPermissionChecker.hasCommentPermission(userId, postId))
            .willReturn(true)
        
        val myUserData = UserQueryData(userId, "나", "my_profile.jpg", "my@test.com")
        val otherUserData = UserQueryData(writerId1, "다른사람", "other_profile.jpg", "other@test.com")
        
        given(userQueryAPI.getUsers(any()))
            .willReturn(listOf(myUserData, otherUserData))
        
        val pagingInformation = PagingInformation(0, 10)
        
        //when
        val result = postCommentDetailReader.getPostCommentDetails(userId, postId, pagingInformation)
        
        //then
        Assertions.assertThat(result).hasSize(2)
        
        val myCommentDetail = result.find { it.id == myComment.id }
        Assertions.assertThat(myCommentDetail).isNotNull
        Assertions.assertThat(myCommentDetail!!.writerName).isEqualTo("나")
        Assertions.assertThat(myCommentDetail.comment).isEqualTo("내가 작성한 댓글")
        Assertions.assertThat(myCommentDetail.isWriter).isTrue()
        
        val otherCommentDetail = result.find { it.id == otherComment.id }
        Assertions.assertThat(otherCommentDetail).isNotNull
        Assertions.assertThat(otherCommentDetail!!.writerName).isEqualTo("다른사람")
        Assertions.assertThat(otherCommentDetail.isWriter).isFalse()
    }
    
    @Test
    fun `댓글 조회 권한이 없으면 예외가 발생한다`() {
        //given
        val userId = 3L
        val postId = 300L
        val writerId1 = 401L
        
        createPostComment(postId, writerId1, "권한 없는 사용자가 볼 수 없는 댓글", false)
        
        given(postPermissionChecker.hasCommentPermission(userId, postId))
            .willReturn(false)
        
        val pagingInformation = PagingInformation(0, 10)
        
        //when & then
        assertThrows<CoreException> {
            postCommentDetailReader.getPostCommentDetails(userId, postId, pagingInformation)
        }
    }
    
    @Test
    fun `페이징이 적용된 댓글 목록을 조회할 수 있다`() {
        //given
        val userId = 4L
        val postId = 400L
        val writerId1 = 501L
        
        val comments = (1..5).map { index ->
            createPostComment(postId, writerId1, "댓글 $index", false)
        }
        
        given(postPermissionChecker.hasCommentPermission(userId, postId))
            .willReturn(true)
        
        val userData = UserQueryData(writerId1, "작성자", "writer.jpg", "writer@test.com")
        given(userQueryAPI.getUsers(any()))
            .willReturn(listOf(userData))
        
        val pagingInformation = PagingInformation(0, 3)
        
        //when
        val result = postCommentDetailReader.getPostCommentDetails(userId, postId, pagingInformation)
        
        //then
        Assertions.assertThat(result).hasSize(3)
        result.forEach { commentDetail ->
            Assertions.assertThat(commentDetail.writerName).isEqualTo("작성자")
            Assertions.assertThat(commentDetail.isCreator).isFalse()
            Assertions.assertThat(commentDetail.isWriter).isFalse()
        }
    }
    
    @Test
    fun `크리에이터와 일반 사용자 댓글을 구분할 수 있다`() {
        //given
        val userId = 5L
        val postId = 500L
        val writerId1 = 601L
        val creatorId = 701L
        
        val userComment = createPostComment(postId, writerId1, "일반 사용자 댓글", false)
        val creatorComment = createPostComment(postId, creatorId, "크리에이터 댓글", true)
        
        given(postPermissionChecker.hasCommentPermission(userId, postId))
            .willReturn(true)
        
        val userData = UserQueryData(writerId1, "일반사용자", "user.jpg", "user@test.com")
        val creatorData = UserQueryData(creatorId, "문제제작자", "creator.jpg", "creator@test.com")
        
        given(userQueryAPI.getUsers(any()))
            .willReturn(listOf(userData, creatorData))
        
        val pagingInformation = PagingInformation(0, 10)
        
        //when
        val result = postCommentDetailReader.getPostCommentDetails(userId, postId, pagingInformation)
        
        //then
        Assertions.assertThat(result).hasSize(2)
        
        val userCommentDetail = result.find { it.id == userComment.id }
        Assertions.assertThat(userCommentDetail).isNotNull
        Assertions.assertThat(userCommentDetail!!.writerName).isEqualTo("일반사용자")
        Assertions.assertThat(userCommentDetail.isCreator).isFalse()
        
        val creatorCommentDetail = result.find { it.id == creatorComment.id }
        Assertions.assertThat(creatorCommentDetail).isNotNull
        Assertions.assertThat(creatorCommentDetail!!.writerName).isEqualTo("문제제작자")
        Assertions.assertThat(creatorCommentDetail.isCreator).isTrue()
    }
    
    private fun createPostComment(
        postId: Long,
        writerId: Long,
        comment: String,
        isCreator: Boolean
    ): PostComment {
        return postCommentRepository.save(
            PostComment.create(
                postId = postId,
                writerId = writerId,
                comment = comment,
                isCreator = isCreator
            )
        )
    }
}
