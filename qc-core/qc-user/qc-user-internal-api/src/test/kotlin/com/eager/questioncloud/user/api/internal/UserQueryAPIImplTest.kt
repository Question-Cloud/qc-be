package com.eager.questioncloud.user.api.internal

import com.eager.questioncloud.user.domain.User
import com.eager.questioncloud.user.domain.UserAccountInformation
import com.eager.questioncloud.user.domain.UserInformation
import com.eager.questioncloud.user.enums.AccountType
import com.eager.questioncloud.user.enums.UserStatus
import com.eager.questioncloud.user.enums.UserType
import com.eager.questioncloud.user.repository.UserRepository
import com.eager.questioncloud.utils.DBCleaner
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
class UserQueryAPIImplTest(
    @Autowired val userQueryAPI: UserQueryAPIImpl,
    @Autowired val userRepository: UserRepository,
    @Autowired val dbCleaner: DBCleaner,
) {
    @AfterEach
    fun tearDown() {
        dbCleaner.cleanUp()
    }
    
    @Test
    fun `단일 사용자 정보를 조회할 수 있다`() {
        //given
        val user = createEmailUser("test@example.com", "password123", "김테스트")
        
        //when
        val userQueryData = userQueryAPI.getUser(user.uid)
        
        //then
        Assertions.assertThat(userQueryData.userId).isEqualTo(user.uid)
        Assertions.assertThat(userQueryData.name).isEqualTo("김테스트")
        Assertions.assertThat(userQueryData.email).isEqualTo("test@example.com")
        Assertions.assertThat(userQueryData.profileImage).isEqualTo("profile.jpg")
    }
    
    @Test
    fun `여러 사용자 정보를 조회할 수 있다`() {
        //given
        val user1 = createEmailUser("user1@test.com", "password1", "사용자1")
        val user2 = createEmailUser("user2@test.com", "password2", "사용자2")
        val user3 = createSocialUser("social@test.com", "kakao123", "소셜사용자")
        
        val userIds = listOf(user1.uid, user2.uid, user3.uid)
        
        //when
        val userQueryDataList = userQueryAPI.getUsers(userIds)
        
        //then
        Assertions.assertThat(userQueryDataList).hasSize(3)
        
        val userData1 = userQueryDataList.find { it.userId == user1.uid }
        Assertions.assertThat(userData1).isNotNull
        Assertions.assertThat(userData1!!.name).isEqualTo("사용자1")
        Assertions.assertThat(userData1.email).isEqualTo("user1@test.com")
        
        val userData2 = userQueryDataList.find { it.userId == user2.uid }
        Assertions.assertThat(userData2).isNotNull
        Assertions.assertThat(userData2!!.name).isEqualTo("사용자2")
        Assertions.assertThat(userData2.email).isEqualTo("user2@test.com")
        
        val userData3 = userQueryDataList.find { it.userId == user3.uid }
        Assertions.assertThat(userData3).isNotNull
        Assertions.assertThat(userData3!!.name).isEqualTo("소셜사용자")
        Assertions.assertThat(userData3.email).isEqualTo("social@test.com")
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
    
    private fun createSocialUser(email: String, socialUid: String, name: String): User {
        val userAccountInformation = UserAccountInformation.createSocialAccountInformation(socialUid, AccountType.KAKAO)
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
