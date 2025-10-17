package com.eager.questioncloud.user.scenario

import com.eager.questioncloud.user.domain.User
import com.eager.questioncloud.user.domain.UserAccountInformation
import com.eager.questioncloud.user.enums.AccountType
import com.eager.questioncloud.user.enums.UserStatus
import com.eager.questioncloud.user.enums.UserType
import com.eager.questioncloud.utils.Fixture
import com.navercorp.fixturemonkey.kotlin.KotlinTypeDefaultArbitraryBuilder
import com.navercorp.fixturemonkey.kotlin.giveMeKotlinBuilder

object UserScenario {
    fun createEmailUser(password: String): User {
        val userAccountInformation = UserAccountInformation.createEmailAccountInformation(password)
        
        return Fixture.fixtureMonkey.giveMeKotlinBuilder<User>()
            .set(User::userAccountInformation, userAccountInformation)
            .set(User::userType, UserType.NormalUser)
            .set(User::userStatus, UserStatus.Active)
            .sample()
    }
    
    fun createSocialUser(): User {
        val userAccountInformation =
            Fixture.fixtureMonkey.giveMeKotlinBuilder<UserAccountInformation>()
                .set(UserAccountInformation::socialUid, "abcdefg")
                .set(UserAccountInformation::accountType, AccountType.KAKAO)
                .set(UserAccountInformation::password, null)
                .sample()
        
        return Fixture.fixtureMonkey.giveMeKotlinBuilder<User>()
            .set(User::userAccountInformation, userAccountInformation)
            .set(User::userType, UserType.NormalUser)
            .set(User::userStatus, UserStatus.Active)
            .sample()
    }
}

fun User.custom(block: KotlinTypeDefaultArbitraryBuilder<User>.() -> KotlinTypeDefaultArbitraryBuilder<User>): User {
    return Fixture.fixtureMonkey.giveMeKotlinBuilder(this)
        .block()
        .sample()
}