package com.eager.questioncloud.core.domain.creator;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RegisteredCreatorEvent {
    private Creator creator;

    public static RegisteredCreatorEvent create(Creator creator) {
        return new RegisteredCreatorEvent(creator);
    }
}
