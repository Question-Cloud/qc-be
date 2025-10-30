package com.eager.questioncloud.user.api.internal

import com.eager.questioncloud.test.utils.DBCleaner
import com.eager.questioncloud.user.domain.User
import com.eager.questioncloud.user.domain.UserAccountInformation
import com.eager.questioncloud.user.domain.UserInformation
import com.eager.questioncloud.user.enums.UserStatus
import com.eager.questioncloud.user.enums.UserType
import com.eager.questioncloud.user.repository.UserRepository
import io.kotest.core.extensions.ApplyExtension
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
@ApplyExtension(SpringExtension::class)
class UserCommandAPIImplTest(
    private val userCommandAPI: UserCommandAPIImpl,
    private val userRepository: UserRepository,
    private val dbCleaner: DBCleaner,
) : BehaviorSpec() {
    
    init {
        afterTest {
            dbCleaner.cleanUp()
        }
        
        Given("일반 사용자가 존재할 때") {
            val user = createEmailUser("test@test.com", "password123", "김테스트")
            When("사용자를 크리에이터로 변환하면") {
                userCommandAPI.toCreator(user.uid)
                
                Then("사용자 타입이 크리에이터로 변경된다") {
                    val updatedUser = userRepository.getUser(user.uid)
                    updatedUser.userType shouldBe UserType.CreatorUser
                }
            }
        }
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
