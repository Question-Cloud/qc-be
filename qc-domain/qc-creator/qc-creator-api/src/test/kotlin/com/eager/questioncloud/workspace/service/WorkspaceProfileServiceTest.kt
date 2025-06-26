package com.eager.questioncloud.workspace.service

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.creator.domain.Creator
import com.eager.questioncloud.creator.infrastructure.repository.CreatorRepository
import com.eager.questioncloud.utils.DBCleaner
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
class WorkspaceProfileServiceTest(
    @Autowired
    private val workspaceProfileService: WorkspaceProfileService,
    @Autowired
    private val creatorRepository: CreatorRepository,
    @Autowired
    private val dbCleaner: DBCleaner,
) {

    @AfterEach
    fun tearDown() {
        dbCleaner.cleanUp()
    }

    @Test
    fun `크리에이터 프로필을 업데이트할 수 있다`() {
        // given
        val userId = 1L
        val mainSubject = "수학"
        val introduction = "안녕하세요, 수학 강사입니다."
        val creator = Creator(userId = userId, mainSubject = "기존 과목", introduction = "기존 소개")
        creatorRepository.save(creator)

        // when
        workspaceProfileService.updateCreatorProfile(userId, mainSubject, introduction)

        // then
        val updatedCreator = creatorRepository.findByUserId(userId)
        assertThat(updatedCreator).isNotNull
        assertThat(updatedCreator?.mainSubject).isEqualTo(mainSubject)
        assertThat(updatedCreator?.introduction).isEqualTo(introduction)
    }
    
    @Test
    fun `me 메서드는 크리에이터 정보를 반환한다`() {
        // given
        val userId = 1L
        val expectedCreator = Creator(userId = userId, mainSubject = "수학", introduction = "안녕하세요")
        creatorRepository.save(expectedCreator)

        // when
        val actualCreator = workspaceProfileService.me(userId)

        // then
        assertThat(actualCreator).isNotNull
        assertThat(actualCreator.userId).isEqualTo(expectedCreator.userId)
        assertThat(actualCreator.mainSubject).isEqualTo(expectedCreator.mainSubject)
        assertThat(actualCreator.introduction).isEqualTo(expectedCreator.introduction)
    }
}