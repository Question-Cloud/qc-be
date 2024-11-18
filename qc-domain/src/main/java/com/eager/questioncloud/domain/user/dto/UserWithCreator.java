package com.eager.questioncloud.domain.user.dto;

import com.eager.questioncloud.domain.creator.Creator;
import com.eager.questioncloud.domain.user.model.User;

public record UserWithCreator(User user, Creator creator) {
}
