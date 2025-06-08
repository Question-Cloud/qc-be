package com.eager.questioncloud.application.api.workspace.service

import com.eager.questioncloud.application.utils.DBCleaner
import com.eager.questioncloud.application.utils.fixture.helper.CreatorFixtureHelper
import com.eager.questioncloud.application.utils.fixture.helper.PostFixtureHelper
import com.eager.questioncloud.application.utils.fixture.helper.QuestionFixtureHelper
import com.eager.questioncloud.application.utils.fixture.helper.UserFixtureHelper
import com.eager.questioncloud.core.common.PagingInformation
import com.eager.questioncloud.core.domain.creator.infrastructure.repository.CreatorRepository
import com.eager.questioncloud.core.domain.post.infrastructure.repository.PostRepository
import com.eager.questioncloud.core.domain.question.infrastructure.repository.QuestionRepository
import com.eager.questioncloud.core.domain.user.enums.UserStatus
import com.eager.questioncloud.core.domain.user.infrastructure.repository.UserRepository
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
class WorkspacePostServiceTest(
    @Autowired val workspacePostService: WorkspacePostService,
    @Autowired val postRepository: PostRepository,
    @Autowired val creatorRepository: CreatorRepository,
    @Autowired val questionRepository: QuestionRepository,
    @Autowired val userRepository: UserRepository,
    @Autowired val dbCleaner: DBCleaner
) {
    @AfterEach
    fun tearDown() {
        dbCleaner.cleanUp()
    }

    @Test
    fun `크리에이터가 만든 문제의 게시글 목록을 조회할 수 있다`() {
        // given
        val user = UserFixtureHelper.createEmailUser(
            "creator@example.com",
            "password123",
            UserStatus.Active,
            userRepository
        )
        val creator = CreatorFixtureHelper.createCreator(user.uid, creatorRepository)

        val question1 =
            QuestionFixtureHelper.createQuestion(creatorId = creator.id, questionRepository = questionRepository)
        val question2 =
            QuestionFixtureHelper.createQuestion(creatorId = creator.id, questionRepository = questionRepository)
        val question3 =
            QuestionFixtureHelper.createQuestion(creatorId = creator.id, questionRepository = questionRepository)

        PostFixtureHelper.createPost(user.uid, question1.id, "첫 번째 포스트", "첫 번째 내용", postRepository)
        PostFixtureHelper.createPost(user.uid, question2.id, "두 번째 포스트", "두 번째 내용", postRepository)
        PostFixtureHelper.createPost(user.uid, question3.id, "세 번째 포스트", "세 번째 내용", postRepository)

        val pagingInformation = PagingInformation(0, 10)

        // when
        val postList = workspacePostService.getCreatorPosts(creator.id, pagingInformation)

        // then
        Assertions.assertThat(postList).hasSize(3)

        val postTitles = postList.map { it.title }
        Assertions.assertThat(postTitles).containsExactlyInAnyOrder(
            "첫 번째 포스트", "두 번째 포스트", "세 번째 포스트"
        )

        postList.forEach { postItem ->
            Assertions.assertThat(postItem.title).isNotBlank()
            Assertions.assertThat(postItem.writer).isNotBlank()
            Assertions.assertThat(postItem.createdAt).isNotNull()
        }
    }

    @Test
    fun `크리에이터가 만든 문제의 게시글 개수를 조회할 수 있다`() {
        // given
        val user = UserFixtureHelper.createEmailUser(
            "creator2@example.com",
            "password123",
            UserStatus.Active,
            userRepository
        )

        val otherUser = UserFixtureHelper.createEmailUser(
            "aaaa@example.com",
            "password123",
            UserStatus.Active,
            userRepository
        )

        val creator = CreatorFixtureHelper.createCreator(user.uid, creatorRepository)
        val otherCreator = CreatorFixtureHelper.createCreator(otherUser.uid, creatorRepository)

        val question =
            QuestionFixtureHelper.createQuestion(creatorId = creator.id, questionRepository = questionRepository)

        val otherQuestion =
            QuestionFixtureHelper.createQuestion(creatorId = otherCreator.id, questionRepository = questionRepository)

        repeat(5) { index ->
            PostFixtureHelper.createPost(
                user.uid,
                question.id,
                "포스트 제목 $index",
                "포스트 내용 $index",
                postRepository
            )
        }

        repeat(5) { index ->
            PostFixtureHelper.createPost(
                otherUser.uid,
                otherQuestion.id,
                "포스트 제목 $index",
                "포스트 내용 $index",
                postRepository
            )
        }

        // when
        val count = workspacePostService.countCreatorPost(creator.id)

        // then
        Assertions.assertThat(count).isEqualTo(5)
    }
}
