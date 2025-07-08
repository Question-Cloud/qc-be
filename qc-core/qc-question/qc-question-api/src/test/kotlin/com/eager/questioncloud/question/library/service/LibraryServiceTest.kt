package com.eager.questioncloud.question.library.service

import com.eager.questioncloud.common.pagination.PagingInformation
import com.eager.questioncloud.question.common.QuestionFilter
import com.eager.questioncloud.question.dto.UserQuestionContent
import com.eager.questioncloud.question.enums.QuestionLevel
import com.eager.questioncloud.question.enums.QuestionSortType
import com.eager.questioncloud.question.library.dto.ContentCreator
import com.eager.questioncloud.question.library.dto.LibraryContent
import com.eager.questioncloud.question.library.implement.LibraryContentReader
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.kotlin.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
class LibraryServiceTest(
    @Autowired val libraryService: LibraryService
) {
    @MockBean
    lateinit var libraryContentReader: LibraryContentReader

    private val userId = 1L
    private val creatorId = 101L

    @Test
    fun `사용자의 문제 목록을 조회할 수 있다`() {
        //given
        val questionFilter = QuestionFilter(sort = QuestionSortType.Latest)
        val pagingInformation = PagingInformation(0, 10)

        val userQuestionContent = UserQuestionContent(
            questionId = 1L,
            creatorId = creatorId,
            title = "수학 문제 1",
            parentCategory = "수학",
            childCategory = "미적분",
            thumbnail = "thumbnail.jpg",
            questionLevel = QuestionLevel.LEVEL3,
            fileUrl = "question.pdf",
            explanationUrl = "explanation.pdf"
        )

        val contentCreator = ContentCreator(
            name = "수학선생님",
            profileImage = "profile.jpg",
            mainSubject = "수학"
        )

        val libraryContent = LibraryContent(userQuestionContent, contentCreator)
        val expectedResult = listOf(libraryContent)

        given(libraryContentReader.getUserQuestions(userId, questionFilter, pagingInformation))
            .willReturn(expectedResult)

        //when
        val result = libraryService.getUserQuestions(userId, questionFilter, pagingInformation)

        //then
        Assertions.assertThat(result).hasSize(1)
        Assertions.assertThat(result[0].content.title).isEqualTo("수학 문제 1")
        Assertions.assertThat(result[0].creator.name).isEqualTo("수학선생님")
        Assertions.assertThat(result[0].creator.mainSubject).isEqualTo("수학")
    }

    @Test
    fun `사용자의 문제 개수를 조회할 수 있다`() {
        //given
        val questionFilter = QuestionFilter(sort = QuestionSortType.Latest)
        val expectedCount = 5

        given(libraryContentReader.countUserQuestions(userId, questionFilter))
            .willReturn(expectedCount)

        //when
        val result = libraryService.countUserQuestions(userId, questionFilter)

        //then
        Assertions.assertThat(result).isEqualTo(5)
    }
}
