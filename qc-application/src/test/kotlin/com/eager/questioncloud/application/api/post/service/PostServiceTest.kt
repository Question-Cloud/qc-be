package com.eager.questioncloud.application.api.post.service

import com.eager.questioncloud.application.utils.DBCleaner
import com.eager.questioncloud.application.utils.fixture.helper.*
import com.eager.questioncloud.core.common.PagingInformation
import com.eager.questioncloud.core.domain.creator.infrastructure.repository.CreatorRepository
import com.eager.questioncloud.core.domain.post.infrastructure.repository.PostRepository
import com.eager.questioncloud.core.domain.post.model.Post
import com.eager.questioncloud.core.domain.post.model.PostContent
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
class PostServiceTest(
    @Autowired val postService: PostService,
    @Autowired val userRepository: UserRepository,
    @Autowired val creatorRepository: CreatorRepository,
    @Autowired val questionRepository: QuestionRepository,
    @Autowired val userQuestionRepository: UserQuestionRepository,
    @Autowired val postRepository: PostRepository,
    @Autowired val dbCleaner: DBCleaner,
) {
    @AfterEach
    fun tearDown() {
        dbCleaner.cleanUp()
    }

    @Test
    fun `크리에이터는 자신의 문제에 게시글을 등록할 수 있다`() {
        //given
        val creatorUser =
            UserFixtureHelper.createEmailUser("creator@test.com", "password123", UserStatus.Active, userRepository)
        val creator = CreatorFixtureHelper.createCreator(creatorUser.uid, creatorRepository)
        val question = QuestionFixtureHelper.createQuestion(creator.id, questionRepository = questionRepository)

        val post = Post.create(
            questionId = question.id,
            writerId = creatorUser.uid,
            postContent = PostContent.create("테스트 게시글", "게시글 내용입니다.", emptyList())
        )

        //when
        val registeredPost = postService.register(post)

        //then
        Assertions.assertThat(registeredPost.writerId).isEqualTo(creatorUser.uid)
        Assertions.assertThat(registeredPost.questionId).isEqualTo(question.id)
        Assertions.assertThat(registeredPost.postContent.title).isEqualTo("테스트 게시글")
    }

    @Test
    fun `문제를 구매한 사용자는 해당 문제에 게시글을 등록할 수 있다`() {
        //given
        val creatorUser =
            UserFixtureHelper.createEmailUser("creator@test.com", "password123", UserStatus.Active, userRepository)
        val purchaserUser =
            UserFixtureHelper.createEmailUser("purchaser@test.com", "password123", UserStatus.Active, userRepository)

        val creator = CreatorFixtureHelper.createCreator(creatorUser.uid, creatorRepository)
        val question = QuestionFixtureHelper.createQuestion(creator.id, questionRepository = questionRepository)
        UserQuestionFixtureHelper.createUserQuestion(purchaserUser.uid, question.id, userQuestionRepository)

        val post = Post.create(
            questionId = question.id,
            writerId = purchaserUser.uid,
            postContent = PostContent.create("구매자 게시글", "구매자가 작성한 게시글 내용입니다.", emptyList())
        )

        //when
        val registeredPost = postService.register(post)

        //then
        Assertions.assertThat(registeredPost.writerId).isEqualTo(purchaserUser.uid)
        Assertions.assertThat(registeredPost.questionId).isEqualTo(question.id)
    }

    @Test
    fun `권한이 없는 사용자는 게시글을 등록할 수 없다`() {
        //given
        val creatorUser =
            UserFixtureHelper.createEmailUser("creator@test.com", "password123", UserStatus.Active, userRepository)
        val unauthorizedUser =
            UserFixtureHelper.createEmailUser("unauthorized@test.com", "password123", UserStatus.Active, userRepository)

        val creator = CreatorFixtureHelper.createCreator(creatorUser.uid, creatorRepository)
        val question = QuestionFixtureHelper.createQuestion(creator.id, questionRepository = questionRepository)

        val post = Post.create(
            questionId = question.id,
            writerId = unauthorizedUser.uid,
            postContent = PostContent.create("권한 없는 게시글", "권한 없는 사용자의 게시글입니다.", emptyList())
        )

        //when & then
        val exception = assertThrows<CoreException> {
            postService.register(post)
        }
        Assertions.assertThat(exception.error).isEqualTo(Error.FORBIDDEN)
    }

    @Test
    fun `권한이 있는 사용자는 게시글 목록을 조회할 수 있다`() {
        //given
        val creatorUser =
            UserFixtureHelper.createEmailUser("creator@test.com", "password123", UserStatus.Active, userRepository)
        val creator = CreatorFixtureHelper.createCreator(creatorUser.uid, creatorRepository)
        val question = QuestionFixtureHelper.createQuestion(creator.id, questionRepository = questionRepository)

        // 여러 개의 게시글 생성
        PostFixtureHelper.createPost(creatorUser.uid, question.id, postRepository)
        PostFixtureHelper.createPost(creatorUser.uid, question.id, postRepository)
        PostFixtureHelper.createPost(creatorUser.uid, question.id, postRepository)

        val pagingInformation = PagingInformation(offset = 0, size = 10)

        //when
        val postList = postService.getPostList(creatorUser.uid, question.id, pagingInformation)

        //then
        Assertions.assertThat(postList).hasSizeGreaterThanOrEqualTo(3)
    }

    @Test
    fun `권한이 없는 사용자는 게시글 목록을 조회할 수 없다`() {
        //given
        val creatorUser =
            UserFixtureHelper.createEmailUser("creator@test.com", "password123", UserStatus.Active, userRepository)
        val unauthorizedUser =
            UserFixtureHelper.createEmailUser("unauthorized@test.com", "password123", UserStatus.Active, userRepository)

        val creator = CreatorFixtureHelper.createCreator(creatorUser.uid, creatorRepository)
        val question = QuestionFixtureHelper.createQuestion(creator.id, questionRepository = questionRepository)

        val pagingInformation = PagingInformation(offset = 0, size = 10)

        //when & then
        val exception = assertThrows<CoreException> {
            postService.getPostList(unauthorizedUser.uid, question.id, pagingInformation)
        }
        Assertions.assertThat(exception.error).isEqualTo(Error.FORBIDDEN)
    }

    @Test
    fun `문제의 게시글 개수를 조회할 수 있다`() {
        //given
        val creatorUser =
            UserFixtureHelper.createEmailUser("creator@test.com", "password123", UserStatus.Active, userRepository)
        val creator = CreatorFixtureHelper.createCreator(creatorUser.uid, creatorRepository)
        val question = QuestionFixtureHelper.createQuestion(creator.id, questionRepository = questionRepository)

        val initialCount = postService.countPost(question.id)

        // 게시글 2개 추가
        PostFixtureHelper.createPost(creatorUser.uid, question.id, postRepository)
        PostFixtureHelper.createPost(creatorUser.uid, question.id, postRepository)

        //when
        val finalCount = postService.countPost(question.id)

        //then
        Assertions.assertThat(finalCount).isEqualTo(initialCount + 2)
    }

    @Test
    fun `권한이 있는 사용자는 게시글 상세를 조회할 수 있다`() {
        //given
        val creatorUser =
            UserFixtureHelper.createEmailUser("creator@test.com", "password123", UserStatus.Active, userRepository)
        val creator = CreatorFixtureHelper.createCreator(creatorUser.uid, creatorRepository)
        val question = QuestionFixtureHelper.createQuestion(creator.id, questionRepository = questionRepository)
        val post = PostFixtureHelper.createPost(creatorUser.uid, question.id, postRepository)

        //when
        val postDetail = postService.getPostDetail(creatorUser.uid, post.id)

        //then
        Assertions.assertThat(postDetail.id).isEqualTo(post.id)
        Assertions.assertThat(postDetail.questionId).isEqualTo(question.id)
    }

    @Test
    fun `권한이 없는 사용자는 게시글 상세를 조회할 수 없다`() {
        //given
        val creatorUser =
            UserFixtureHelper.createEmailUser("creator@test.com", "password123", UserStatus.Active, userRepository)
        val unauthorizedUser =
            UserFixtureHelper.createEmailUser("unauthorized@test.com", "password123", UserStatus.Active, userRepository)

        val creator = CreatorFixtureHelper.createCreator(creatorUser.uid, creatorRepository)
        val question = QuestionFixtureHelper.createQuestion(creator.id, questionRepository = questionRepository)
        val post = PostFixtureHelper.createPost(creatorUser.uid, question.id, postRepository)

        //when & then
        val exception = assertThrows<CoreException> {
            postService.getPostDetail(unauthorizedUser.uid, post.id)
        }
        Assertions.assertThat(exception.error).isEqualTo(Error.FORBIDDEN)
    }

    @Test
    @Transactional
    fun `게시글 작성자는 자신의 게시글을 수정할 수 있다`() {
        //given
        val creatorUser =
            UserFixtureHelper.createEmailUser("creator@test.com", "password123", UserStatus.Active, userRepository)
        val creator = CreatorFixtureHelper.createCreator(creatorUser.uid, creatorRepository)
        val question = QuestionFixtureHelper.createQuestion(creator.id, questionRepository = questionRepository)
        val post = PostFixtureHelper.createPost(creatorUser.uid, question.id, postRepository)

        val newContent = PostContent.create("수정된 제목", "수정된 게시글 내용입니다.", emptyList())

        //when
        postService.modify(post.id, creatorUser.uid, newContent)

        //then
        val modifiedPost = postRepository.findById(post.id)
        Assertions.assertThat(modifiedPost.postContent.content).isEqualTo("수정된 게시글 내용입니다.")
    }

    @Test
    @Transactional
    fun `게시글 작성자는 자신의 게시글을 삭제할 수 있다`() {
        //given
        val creatorUser =
            UserFixtureHelper.createEmailUser("creator@test.com", "password123", UserStatus.Active, userRepository)
        val creator = CreatorFixtureHelper.createCreator(creatorUser.uid, creatorRepository)
        val question = QuestionFixtureHelper.createQuestion(creator.id, questionRepository = questionRepository)
        val post = PostFixtureHelper.createPost(creatorUser.uid, question.id, postRepository)

        val initialCount = postService.countPost(question.id)

        //when
        postService.delete(post.id, creatorUser.uid)

        //then
        val finalCount = postService.countPost(question.id)
        Assertions.assertThat(finalCount).isEqualTo(initialCount - 1)
    }
}
