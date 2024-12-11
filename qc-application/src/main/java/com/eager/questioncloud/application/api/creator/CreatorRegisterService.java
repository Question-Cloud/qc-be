package com.eager.questioncloud.application.api.creator;

import com.eager.questioncloud.core.domain.creator.event.RegisteredCreatorEvent;
import com.eager.questioncloud.core.domain.creator.model.Creator;
import com.eager.questioncloud.core.domain.creator.model.CreatorProfile;
import com.eager.questioncloud.core.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreatorRegisterService {
    private final CreatorRegister creatorRegister;
    private final ApplicationEventPublisher applicationEventPublisher;

    public Creator register(User user, CreatorProfile creatorProfile) {
        Creator creator = creatorRegister.register(user, creatorProfile);
        applicationEventPublisher.publishEvent(RegisteredCreatorEvent.create(creator));
        return creator;
    }
}
