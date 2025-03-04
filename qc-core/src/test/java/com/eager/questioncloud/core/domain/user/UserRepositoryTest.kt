package com.eager.questioncloud.core.domain.user

import com.eager.questioncloud.core.domain.user.dto.CreateUser
import com.eager.questioncloud.core.domain.user.enums.AccountType
import com.eager.questioncloud.core.domain.user.enums.UserStatus
import com.eager.questioncloud.core.domain.user.enums.UserType
import com.eager.questioncloud.core.domain.user.infrastructure.repository.UserRepository
import com.eager.questioncloud.core.domain.user.model.User.Companion.create
import com.eager.questioncloud.core.domain.user.model.UserAccountInformation.Companion.createEmailAccountInformation
import com.eager.questioncloud.core.domain.user.model.UserInformation.Companion.create
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@ActiveProfiles("test")
@Transactional
internal class UserRepositoryTest(
    @Autowired
    private val userRepository: UserRepository
) {
    @Test
    @DisplayName("이메일이 일치하는 유저를 조회한다.")
    fun 이메일이_일치하는_유저를_조회한다() {
        //given
        val email = "test@test.com"
        val password = "qwer1234"

        val createUser = CreateUser(email, password, null, AccountType.EMAIL, "01012345678", "김승환")
        val userAccountInformation = createEmailAccountInformation(password)
        val userInformation = create(createUser)
        userRepository.save(
            create(
                userAccountInformation,
                userInformation,
                UserType.NormalUser,
                UserStatus.Active
            )
        )

        //when
        val user = userRepository.getUserByEmail(email)

        //then
        Assertions.assertThat(user.userInformation.email).isEqualTo(email)
    }
}