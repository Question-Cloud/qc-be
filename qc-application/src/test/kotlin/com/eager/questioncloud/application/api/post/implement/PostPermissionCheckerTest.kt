package com.eager.questioncloud.application.api.post.implement

import com.eager.questioncloud.application.utils.DBCleaner
import com.eager.questioncloud.application.utils.fixture.helper.CreatorFixtureHelper
import com.eager.questioncloud.application.utils.fixture.helper.PostFixtureHelper
import com.eager.questioncloud.application.utils.fixture.helper.QuestionFixtureHelper
import com.eager.questioncloud.application.utils.fixture.helper.UserFixtureHelper
import com.eager.questioncloud.application.utils.fixture.helper.UserQuestionFixtureHelper
import com.eager.questioncloud.core.domain.creator.infrastructure.repository.CreatorRepository
import com.eager.questioncloud.core.domain.post.infrastructure.repository.PostRepository
import com.eager.questioncloud.core.domain.question.infrastructure.repository.QuestionRepository
import com.eager.questioncloud.core.domain.user.enums.UserStatus
import com.eager.questioncloud.core.domain.user.infrastructure.repository.UserRepository
import com.eager.questioncloud.core.domain.userquestion.infrastructure.repository.UserQuestionRepository
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
class PostPermissionCheckerTest(
    @Autowired val postPermissionChecker: PostPermissionChecker,
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
    fun `크리에이터는 자신의 문제 게시글에 대한 접근 권한을 가진다`() {
        //given
        val creatorUser = UserFixtureHelper.createEmailUser("creator@test.com", "password123", UserStatus.Active, userRepository)
        val creator = CreatorFixtureHelper.createCreator(creatorUser.uid, creatorRepository)
        val question = QuestionFixtureHelper.createQuestion(creator.id, questionRepository = questionRepository)

        //when
        val hasPermission = postPermissionChecker.hasPermission(creatorUser.uid, question.id)

        //then
        Assertions.assertThat(hasPermission).isTrue()
    }

    @Test
    fun `문제 구매자는 해당 문제 게시글에 대한 접근 권한을 가진다`() {
        //given
        val creatorUser = UserFixtureHelper.createEmailUser("creator@test.com", "password123", UserStatus.Active, userRepository)
        val purchaserUser = UserFixtureHelper.createEmailUser("purchaser@test.com", "password123", UserStatus.Active, userRepository)
        
        val creator = CreatorFixtureHelper.createCreator(creatorUser.uid, creatorRepository)
        val question = QuestionFixtureHelper.createQuestion(creator.id, questionRepository = questionRepository)
        
        UserQuestionFixtureHelper.createUserQuestion(purchaserUser.uid, question.id, userQuestionRepository)

        //when
        val hasPermission = postPermissionChecker.hasPermission(purchaserUser.uid, question.id)

        //then
        Assertions.assertThat(hasPermission).isTrue()
    }

    @Test
    fun `문제를 구매하지 않은 사용자는 해당 문제 게시글에 대한 접근 권한을 가지지 않는다`() {
        //given
        val creatorUser = UserFixtureHelper.createEmailUser("creator@test.com", "password123", UserStatus.Active, userRepository)
        val otherUser = UserFixtureHelper.createEmailUser("other@test.com", "password123", UserStatus.Active, userRepository)
        
        val creator = CreatorFixtureHelper.createCreator(creatorUser.uid, creatorRepository)
        val question = QuestionFixtureHelper.createQuestion(creator.id, questionRepository = questionRepository)

        //when
        val hasPermission = postPermissionChecker.hasPermission(otherUser.uid, question.id)

        //then
        Assertions.assertThat(hasPermission).isFalse()
    }

    @Test
    fun `크리에이터는 자신의 문제 게시글에 대한 댓글 권한을 가진다`() {
        //given
        val creatorUser = UserFixtureHelper.createEmailUser("creator@test.com", "password123", UserStatus.Active, userRepository)
        val creator = CreatorFixtureHelper.createCreator(creatorUser.uid, creatorRepository)
        val question = QuestionFixtureHelper.createQuestion(creator.id, questionRepository = questionRepository)
        val post = PostFixtureHelper.createPost(creatorUser.uid, question.id, postRepository)

        //when
        val hasCommentPermission = postPermissionChecker.hasCommentPermission(creatorUser.uid, post.id)

        //then
        Assertions.assertThat(hasCommentPermission).isTrue()
    }

    @Test
    fun `문제 구매자는 해당 문제 게시글에 대한 댓글 권한을 가진다`() {
        //given
        val creatorUser = UserFixtureHelper.createEmailUser("creator@test.com", "password123", UserStatus.Active, userRepository)
        val purchaserUser = UserFixtureHelper.createEmailUser("purchaser@test.com", "password123", UserStatus.Active, userRepository)
        
        val creator = CreatorFixtureHelper.createCreator(creatorUser.uid, creatorRepository)
        val question = QuestionFixtureHelper.createQuestion(creator.id, questionRepository = questionRepository)
        val post = PostFixtureHelper.createPost(creatorUser.uid, question.id, postRepository)
        
        UserQuestionFixtureHelper.createUserQuestion(purchaserUser.uid, question.id, userQuestionRepository)

        //when
        val hasCommentPermission = postPermissionChecker.hasCommentPermission(purchaserUser.uid, post.id)

        //then
        Assertions.assertThat(hasCommentPermission).isTrue()
    }

    @Test
    fun `게시글 접근 권한이 없는 사용자는 댓글 권한을 가지지 않는다`() {
        //given
        val creatorUser = UserFixtureHelper.createEmailUser("creator@test.com", "password123", UserStatus.Active, userRepository)
        val otherUser = UserFixtureHelper.createEmailUser("other@test.com", "password123", UserStatus.Active, userRepository)
        
        val creator = CreatorFixtureHelper.createCreator(creatorUser.uid, creatorRepository)
        val question = QuestionFixtureHelper.createQuestion(creator.id, questionRepository = questionRepository)
        val post = PostFixtureHelper.createPost(creatorUser.uid, question.id, postRepository)

        //when
        val hasCommentPermission = postPermissionChecker.hasCommentPermission(otherUser.uid, post.id)

        //then
        Assertions.assertThat(hasCommentPermission).isFalse()
    }

    @Test
    fun `크리에이터는 자신의 문제에 대해 크리에이터 권한을 가진다`() {
        //given
        val creatorUser = UserFixtureHelper.createEmailUser("creator@test.com", "password123", UserStatus.Active, userRepository)
        val creator = CreatorFixtureHelper.createCreator(creatorUser.uid, creatorRepository)
        val question = QuestionFixtureHelper.createQuestion(creator.id, questionRepository = questionRepository)

        //when
        val isCreator = postPermissionChecker.isCreator(creatorUser.uid, question.id)

        //then
        Assertions.assertThat(isCreator).isTrue()
    }

    @Test
    fun `다른 사용자는 해당 문제에 대해 크리에이터 권한을 가지지 않는다`() {
        //given
        val creatorUser = UserFixtureHelper.createEmailUser("creator@test.com", "password123", UserStatus.Active, userRepository)
        val otherUser = UserFixtureHelper.createEmailUser("other@test.com", "password123", UserStatus.Active, userRepository)
        
        val creator = CreatorFixtureHelper.createCreator(creatorUser.uid, creatorRepository)
        val question = QuestionFixtureHelper.createQuestion(creator.id, questionRepository = questionRepository)

        //when
        val isCreator = postPermissionChecker.isCreator(otherUser.uid, question.id)

        //then
        Assertions.assertThat(isCreator).isFalse()
    }
}
