package com.eager.questioncloud.core.domain.user;

import com.eager.questioncloud.core.domain.creator.Creator;

public record UserWithCreator(User user, Creator creator) {
}
