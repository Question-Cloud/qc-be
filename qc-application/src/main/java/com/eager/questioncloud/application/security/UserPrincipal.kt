package com.eager.questioncloud.application.security

import com.eager.questioncloud.core.domain.creator.model.Creator
import com.eager.questioncloud.core.domain.user.enums.UserType
import com.eager.questioncloud.core.domain.user.model.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import java.io.Serializable

class UserPrincipal(
    val user: User,
    val creator: Creator?
) : Serializable {
    val authorities: Collection<GrantedAuthority>
        get() {
            val authorities: MutableList<GrantedAuthority> = ArrayList()
            if (user.uid == -1L) {
                authorities.add(SimpleGrantedAuthority("ROLE_GUEST"))
            } else if (user.userType == UserType.NormalUser) {
                authorities.add(SimpleGrantedAuthority("ROLE_NormalUser"))
            } else if (user.userType == UserType.CreatorUser) {
                authorities.add(SimpleGrantedAuthority("ROLE_CreatorUser"))
            }
            return authorities
        }

    companion object {
        fun create(user: User, creator: Creator?): UserPrincipal {
            return UserPrincipal(user, creator)
        }

        fun guest(): UserPrincipal {
            return UserPrincipal(User.guest(), null)
        }
    }
}
