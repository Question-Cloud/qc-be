package com.eager.questioncloud.core.domain.user.dto;

import com.eager.questioncloud.core.domain.questionhub.creator.model.Creator;
import com.eager.questioncloud.core.domain.user.model.User;

public record UserWithCreator(User user, Creator creator) {
}
