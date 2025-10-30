package com.eager.questioncloud.user.profile.implement

import com.eager.questioncloud.user.repository.UserRepository
import com.eager.questioncloud.user.scenario.UserScenario
import com.eager.questioncloud.test.utils.DBCleaner
import io.kotest.core.extensions.ApplyExtension
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
@ApplyExtension(SpringExtension::class)
class UserProfileUpdaterTest(
    private val userProfileUpdater: UserProfileUpdater,
    private val userRepository: UserRepository,
    private val dbCleaner: DBCleaner,
) : BehaviorSpec() {
    init {
        afterTest {
            dbCleaner.cleanUp()
        }
        
        Given("사용자 정보 수정") {
            val user = userRepository.save(UserScenario.createEmailUser("password"))
            val newName = "Updated Name"
            val newProfileImage = "https://example.com/new-profile.jpg"
            
            When("사용자 정보를 수정하면") {
                userProfileUpdater.updateUserInformation(user.uid, newName, newProfileImage)
                
                Then("사용자 정보가 수정된다") {
                    val updatedUser = userRepository.getUser(user.uid)
                    updatedUser.userInformation.name shouldBe newName
                    updatedUser.userInformation.profileImage shouldBe newProfileImage
                }
            }
        }
    }
}