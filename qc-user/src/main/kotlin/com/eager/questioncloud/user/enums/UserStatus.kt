package com.eager.questioncloud.user.enums

enum class UserStatus(val value: String) {
    PendingEmailVerification("PendingEmailVerification"), Active("Active"), Deleted("Deleted"), Ban("Ban")
}
