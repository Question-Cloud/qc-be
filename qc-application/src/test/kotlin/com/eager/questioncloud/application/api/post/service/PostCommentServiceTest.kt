package com.eager.questioncloud.application.api.post.service

import com.eager.questioncloud.application.utils.DBCleaner
import com.eager.questioncloud.application.utils.fixture.helper.*
import com.eager.questioncloud.core.common.PagingInformation
import com.eager.questioncloud.core.domain.creator.infrastructure.repository.CreatorRepository
import com.eager.questioncloud.core.domain.post.infrastructure.repository.PostCommentRepository
import com.eager.questioncloud.core.domain.post.infrastructure.repository.PostRepository
import com.eager.questioncloud.core.domain.post.model.PostComment
import com.eager.questioncloud.core.domain.question.infrastructure.repository.QuestionRepository
import com.eager.questioncloud.core.domain.user.enums.UserStatus
import com.eager.questioncloud.core.domain.user.infrastructure.repository.UserRepository
import com.eager.questioncloud.core.domain.userquestion.infrastructure.repository.UserQuestionRepository
import com.eager.questioncloud.core.exception.CoreException
import com.eager.questioncloud.core.exception.Error
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@ActiveProfiles("test")
class PostCommentServiceTest(
    @Autowired val postCommentService: PostCommentService,
    @Autowired val userRepository: UserRepository,
    @Autowired val creatorRepository: CreatorRepository,
    @Autowired val questionRepository: QuestionRepository,
    @Autowired val userQuestionRepository: UserQuestionRepository,
    @Autowired val postRepository: PostRepository,
    @Autowired val postCommentRepository: PostCommentRepository,
    @Autowired val dbCleaner: DBCleaner,
) {
    @AfterEach
    fun tearDown() {
        dbCleaner.cleanUp()
    }

    @Test
    fun `크리에이터는 자신의 문제 게시글에 댓글을 작성할 수 있다`() {
        //given
        val creatorUser =
            UserFixtureHelper.createEmailUser("creator@test.com", "password123", UserStatus.Active, userRepository)
        val creator = CreatorFixtureHelper.createCreator(creatorUser.uid, creatorRepository)
        val question = QuestionFixtureHelper.createQuestion(creator.id, questionRepository = questionRepository)
        val post = PostFixtureHelper.createPost(creatorUser.uid, question.id, postRepository)

        val postComment = PostComment.create(
            postId = post.id,
            writerId = creatorUser.uid,
            comment = "크리에이터가 작성한 댓글입니다."
        )

        //when
        val addedComment = postCommentService.addPostComment(postComment)

        //then
        Assertions.assertThat(addedComment.writerId).isEqualTo(creatorUser.uid)
        Assertions.assertThat(addedComment.postId).isEqualTo(post.id)
        Assertions.assertThat(addedComment.comment).isEqualTo("크리에이터가 작성한 댓글입니다.")
    }

    @Test
    fun `문제 구매자는 해당 문제 게시글에 댓글을 작성할 수 있다`() {
        //given
        val creatorUser =
            UserFixtureHelper.createEmailUser("creator@test.com", "password123", UserStatus.Active, userRepository)
        val purchaserUser =
            UserFixtureHelper.createEmailUser("purchaser@test.com", "password123", UserStatus.Active, userRepository)

        val creator = CreatorFixtureHelper.createCreator(creatorUser.uid, creatorRepository)
        val question = QuestionFixtureHelper.createQuestion(creator.id, questionRepository = questionRepository)
        val post = PostFixtureHelper.createPost(creatorUser.uid, question.id, postRepository)

        UserQuestionFixtureHelper.createUserQuestion(purchaserUser.uid, question.id, userQuestionRepository)

        val postComment = PostComment.create(
            postId = post.id,
            writerId = purchaserUser.uid,
            comment = "구매자가 작성한 댓글입니다."
        )

        //when
        val addedComment = postCommentService.addPostComment(postComment)

        //then
        Assertions.assertThat(addedComment.writerId).isEqualTo(purchaserUser.uid)
        Assertions.assertThat(addedComment.postId).isEqualTo(post.id)
        Assertions.assertThat(addedComment.comment).isEqualTo("구매자가 작성한 댓글입니다.")
    }

    @Test
    fun `댓글 권한이 없는 사용자는 댓글을 작성할 수 없다`() {
        //given
        val creatorUser =
            UserFixtureHelper.createEmailUser("creator@test.com", "password123", UserStatus.Active, userRepository)
        val unauthorizedUser =
            UserFixtureHelper.createEmailUser("unauthorized@test.com", "password123", UserStatus.Active, userRepository)

        val creator = CreatorFixtureHelper.createCreator(creatorUser.uid, creatorRepository)
        val question = QuestionFixtureHelper.createQuestion(creator.id, questionRepository = questionRepository)
        val post = PostFixtureHelper.createPost(creatorUser.uid, question.id, postRepository)

        val postComment = PostComment.create(
            postId = post.id,
            writerId = unauthorizedUser.uid,
            comment = "권한 없는 댓글입니다."
        )

        //when & then
        val exception = assertThrows<CoreException> {
            postCommentService.addPostComment(postComment)
        }
        Assertions.assertThat(exception.error).isEqualTo(Error.FORBIDDEN)
    }

    @Test
    @Transactional
    fun `댓글 작성자는 자신의 댓글을 수정할 수 있다`() {
        //given
        val creatorUser =
            UserFixtureHelper.createEmailUser("creator@test.com", "password123", UserStatus.Active, userRepository)
        val creator = CreatorFixtureHelper.createCreator(creatorUser.uid, creatorRepository)
        val question = QuestionFixtureHelper.createQuestion(creator.id, questionRepository = questionRepository)
        val post = PostFixtureHelper.createPost(creatorUser.uid, question.id, postRepository)

        val originalComment = PostComment.create(
            postId = post.id,
            writerId = creatorUser.uid,
            comment = "원본 댓글입니다."
        )
        val savedComment = postCommentRepository.save(originalComment)

        //when
        postCommentService.modifyPostComment(savedComment.id, creatorUser.uid, "수정된 댓글입니다.")

        //then
        val pagingInformation = PagingInformation(offset = 0, size = 10)
        val comments = postCommentService.getPostCommentDetails(post.id, creatorUser.uid, pagingInformation)
        val modifiedComment = comments.find { it.id == savedComment.id }

        Assertions.assertThat(modifiedComment).isNotNull
        Assertions.assertThat(modifiedComment!!.comment).isEqualTo("수정된 댓글입니다.")
    }

    @Test
    @Transactional
    fun `댓글 작성자는 자신의 댓글을 삭제할 수 있다`() {
        //given
        val creatorUser =
            UserFixtureHelper.createEmailUser("creator@test.com", "password123", UserStatus.Active, userRepository)
        val creator = CreatorFixtureHelper.createCreator(creatorUser.uid, creatorRepository)
        val question = QuestionFixtureHelper.createQuestion(creator.id, questionRepository = questionRepository)
        val post = PostFixtureHelper.createPost(creatorUser.uid, question.id, postRepository)

        val postComment = PostComment.create(
            postId = post.id,
            writerId = creatorUser.uid,
            comment = "삭제될 댓글입니다."
        )
        val savedComment = postCommentRepository.save(postComment)

        val initialCount = postCommentService.count(post.id)

        //when
        postCommentService.deletePostComment(savedComment.id, creatorUser.uid)

        //then
        val finalCount = postCommentService.count(post.id)
        Assertions.assertThat(finalCount).isEqualTo(initialCount - 1)
    }

    @Test
    fun `권한이 있는 사용자는 게시글의 댓글 목록을 조회할 수 있다`() {
        //given
        val creatorUser =
            UserFixtureHelper.createEmailUser("creator@test.com", "password123", UserStatus.Active, userRepository)
        val creator = CreatorFixtureHelper.createCreator(creatorUser.uid, creatorRepository)
        val question = QuestionFixtureHelper.createQuestion(creator.id, questionRepository = questionRepository)
        val post = PostFixtureHelper.createPost(creatorUser.uid, question.id, postRepository)

        // 여러 개의 댓글 생성
        repeat(3) { index ->
            val comment = PostComment.create(
                postId = post.id,
                writerId = creatorUser.uid,
                comment = "댓글 $index"
            )
            postCommentRepository.save(comment)
        }

        val pagingInformation = PagingInformation(offset = 0, size = 10)

        //when
        val comments = postCommentService.getPostCommentDetails(post.id, creatorUser.uid, pagingInformation)

        //then
        Assertions.assertThat(comments).hasSizeGreaterThanOrEqualTo(3)
    }

    @Test
    fun `댓글 권한이 없는 사용자는 댓글 목록을 조회할 수 없다`() {
        //given
        val creatorUser =
            UserFixtureHelper.createEmailUser("creator@test.com", "password123", UserStatus.Active, userRepository)
        val unauthorizedUser =
            UserFixtureHelper.createEmailUser("unauthorized@test.com", "password123", UserStatus.Active, userRepository)

        val creator = CreatorFixtureHelper.createCreator(creatorUser.uid, creatorRepository)
        val question = QuestionFixtureHelper.createQuestion(creator.id, questionRepository = questionRepository)
        val post = PostFixtureHelper.createPost(creatorUser.uid, question.id, postRepository)

        val pagingInformation = PagingInformation(offset = 0, size = 10)

        //when & then
        val exception = assertThrows<CoreException> {
            postCommentService.getPostCommentDetails(post.id, unauthorizedUser.uid, pagingInformation)
        }
        Assertions.assertThat(exception.error).isEqualTo(Error.FORBIDDEN)
    }

    @Test
    fun `게시글의 댓글 개수를 조회할 수 있다`() {
        //given
        val creatorUser =
            UserFixtureHelper.createEmailUser("creator@test.com", "password123", UserStatus.Active, userRepository)
        val creator = CreatorFixtureHelper.createCreator(creatorUser.uid, creatorRepository)
        val question = QuestionFixtureHelper.createQuestion(creator.id, questionRepository = questionRepository)
        val post = PostFixtureHelper.createPost(creatorUser.uid, question.id, postRepository)

        val initialCount = postCommentService.count(post.id)

        // 댓글 2개 추가
        repeat(2) { index ->
            val comment = PostComment.create(
                postId = post.id,
                writerId = creatorUser.uid,
                comment = "댓글 $index"
            )
            postCommentRepository.save(comment)
        }

        //when
        val finalCount = postCommentService.count(post.id)

        //then
        Assertions.assertThat(finalCount).isEqualTo(initialCount + 2)
    }
}
