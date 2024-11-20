package com.eager.questioncloud.domain.user;

import com.eager.questioncloud.domain.creator.Creator;

public record UserWithCreator(User user, Creator creator) {
}
