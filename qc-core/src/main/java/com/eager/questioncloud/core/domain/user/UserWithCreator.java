package com.eager.questioncloud.core.domain.user;

import com.eager.questioncloud.core.domain.creator.model.Creator;

public record UserWithCreator(User user, Creator creator) {
}
