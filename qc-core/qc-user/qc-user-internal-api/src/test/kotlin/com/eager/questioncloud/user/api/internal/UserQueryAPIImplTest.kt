package com.eager.questioncloud.user.api.internal

import com.eager.questioncloud.test.utils.DBCleaner
import com.eager.questioncloud.user.domain.User
import com.eager.questioncloud.user.domain.UserAccountInformation
import com.eager.questioncloud.user.domain.UserInformation
import com.eager.questioncloud.user.enums.AccountType
import com.eager.questioncloud.user.enums.UserStatus
import com.eager.questioncloud.user.enums.UserType
import com.eager.questioncloud.user.repository.UserRepository
import io.kotest.core.extensions.ApplyExtension
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
@ApplyExtension(SpringExtension::class)
class UserQueryAPIImplTest(
    private val userQueryAPI: UserQueryAPIImpl,
    private val userRepository: UserRepository,
    private val dbCleaner: DBCleaner,
) : BehaviorSpec() {

    init {
        afterTest {
            dbCleaner.cleanUp()
        }

        Given("사용자가 존재할 때") {
            val user = createEmailUser("test@example.com", "password123", "김테스트")

            When("사용자 정보를 조회하면") {
                val userQueryData = userQueryAPI.getUser(user.uid)

                Then("해당 사용자의 정보가 반환된다") {
                    userQueryData.userId shouldBe user.uid
                    userQueryData.name shouldBe "김테스트"
                    userQueryData.email shouldBe "test@example.com"
                    userQueryData.profileImage shouldBe "profile.jpg"
                }
            }
        }

        Given("여러 사용자가 존재할 때") {
            val user1 = createEmailUser("user1@test.com", "password1", "사용자1")
            val user2 = createEmailUser("user2@test.com", "password2", "사용자2")
            val user3 = createSocialUser("social@test.com", "kakao123", "소셜사용자")

            val userIds = listOf(user1.uid, user2.uid, user3.uid)

            When("여러 사용자 정보를 조회하면") {
                val userQueryDataList = userQueryAPI.getUsers(userIds)

                Then("모든 사용자의 정보가 반환된다") {
                    userQueryDataList shouldHaveSize 3

                    val userData1 = userQueryDataList.find { it.userId == user1.uid }
                    userData1 shouldNotBe null
                    userData1!!.name shouldBe "사용자1"
                    userData1.email shouldBe "user1@test.com"

                    val userData2 = userQueryDataList.find { it.userId == user2.uid }
                    userData2 shouldNotBe null
                    userData2!!.name shouldBe "사용자2"
                    userData2.email shouldBe "user2@test.com"

                    val userData3 = userQueryDataList.find { it.userId == user3.uid }
                    userData3 shouldNotBe null
                    userData3!!.name shouldBe "소셜사용자"
                    userData3.email shouldBe "social@test.com"
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
