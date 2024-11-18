package com.eager.questioncloud.domain.user.dto;

import com.eager.questioncloud.domain.creator.model.Creator;
import com.eager.questioncloud.domain.user.model.User;

public record UserWithCreator(User user, Creator creator) {
}
