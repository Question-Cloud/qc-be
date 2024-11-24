package com.eager.questioncloud.core.domain.creator;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RegisteredCreatorEvent {
    private Creator creator;

    public static RegisteredCreatorEvent create(Creator creator) {
        return new RegisteredCreatorEvent(creator);
    }
}
