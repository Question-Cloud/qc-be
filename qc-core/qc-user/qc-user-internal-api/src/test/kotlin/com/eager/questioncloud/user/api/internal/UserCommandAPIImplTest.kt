package com.eager.questioncloud.user.api.internal

import com.eager.questioncloud.user.domain.User
import com.eager.questioncloud.user.domain.UserAccountInformation
import com.eager.questioncloud.user.domain.UserInformation
import com.eager.questioncloud.user.enums.UserStatus
import com.eager.questioncloud.user.enums.UserType
import com.eager.questioncloud.user.repository.UserRepository
import com.eager.questioncloud.test.utils.DBCleaner
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
class UserCommandAPIImplTest(
    @Autowired val userCommandAPI: UserCommandAPIImpl,
    @Autowired val userRepository: UserRepository,
    @Autowired val dbCleaner: DBCleaner,
) {
    @AfterEach
    fun tearDown() {
        dbCleaner.cleanUp()
    }
    
    @Test
    fun `일반 사용자를 크리에이터로 변환할 수 있다`() {
        //given
        val user = createEmailUser("test@test.com", "password123", "김테스트")
        
        // 초기 상태 확인
        Assertions.assertThat(user.userType).isEqualTo(UserType.NormalUser)
        
        //when
        userCommandAPI.toCreator(user.uid)
        
        //then
        val updatedUser = userRepository.getUser(user.uid)
        Assertions.assertThat(updatedUser.userType).isEqualTo(UserType.CreatorUser)
    }
    
    private fun createEmailUser(email: String, password: String, name: String): User {
        val userAccountInformation = UserAccountInformation.createEmailAccountInformation(password)
        val userInformation = UserInformation(email, "01012345678", name, "profile.jpg")
        
        return userRepository.save(
            User.create(
                userAccountInformation = userAccountInformation,
                userInformation = userInformation,
                userType = UserType.NormalUser,
                userStatus = UserStatus.Active
            )
        )
    }
}
