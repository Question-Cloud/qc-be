package com.eager.questioncloud.application.api.creator;

import com.eager.questioncloud.domain.creator.Creator;
import com.eager.questioncloud.domain.creator.CreatorProfile;
import com.eager.questioncloud.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreatorRegisterService {
    private final CreatorRegister creatorRegister;
    private final ApplicationEventPublisher applicationEventPublisher;

    //TODO Event 처리 (Handler)
    public Creator register(User user, CreatorProfile creatorProfile) {
        Creator creator = creatorRegister.register(user, creatorProfile);
        applicationEventPublisher.publishEvent(RegisteredCreatorEvent.create(creator));
        return creator;
    }
}
