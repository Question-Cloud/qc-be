package com.eager.questioncloud.workspace.implement

import com.eager.questioncloud.creator.repository.CreatorRepository
import com.eager.questioncloud.user.api.internal.UserCommandAPI
import com.eager.questioncloud.utils.DBCleaner
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.then
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
class WorkspaceCreatorRegisterTest(
    @Autowired val workspaceCreatorRegister: WorkspaceCreatorRegister,
    @Autowired val creatorRepository: CreatorRepository,
    @Autowired val dbCleaner: DBCleaner,
) {
    @MockBean
    lateinit var userCommandAPI: UserCommandAPI
    
    private val userId = 1L
    
    @AfterEach
    fun tearDown() {
        dbCleaner.cleanUp()
    }
    
    @Test
    fun `워크스페이스에서 크리에이터를 등록할 수 있다`() {
        //given
        val mainSubject = "수학"
        val introduction = "안녕하세요. 수학 크리에이터입니다."
        
        //when
        val newCreator = workspaceCreatorRegister.register(userId, mainSubject, introduction)
        
        //then
        val foundCreator = creatorRepository.findById(newCreator.id)
        Assertions.assertThat(foundCreator).isNotNull
        Assertions.assertThat(foundCreator.userId).isEqualTo(userId)
        Assertions.assertThat(foundCreator.mainSubject).isEqualTo(mainSubject)
        Assertions.assertThat(foundCreator.introduction).isEqualTo(introduction)
        
        then(userCommandAPI).should().toCreator(userId)
    }
}