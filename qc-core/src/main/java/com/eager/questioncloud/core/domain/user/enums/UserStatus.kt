package com.eager.questioncloud.core.domain.user.enums

enum class UserStatus(val value: String) {
    PendingEmailVerification("PendingEmailVerification"), Active("Active"), Deleted("Deleted"), Ban("Ban")
}
