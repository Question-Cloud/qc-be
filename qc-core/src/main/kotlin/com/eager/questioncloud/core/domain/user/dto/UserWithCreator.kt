package com.eager.questioncloud.core.domain.user.dto

import com.eager.questioncloud.core.domain.creator.model.Creator
import com.eager.questioncloud.core.domain.user.model.User

class UserWithCreator(
    val user: User,
    val creator: Creator?,
)
